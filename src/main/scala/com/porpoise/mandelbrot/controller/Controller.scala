package com.porpoise.mandelbrot.controller
import scala.actors.Actor
import com.porpoise.mandelbrot.actors.StoppableActor
import com.porpoise.mandelbrot.io.InputCommand
import com.porpoise.mandelbrot.model.GetStateRequest
import com.porpoise.mandelbrot.model.GetStateResponse
import com.porpoise.mandelbrot.model.MandelbrotResult
import com.porpoise.mandelbrot.model.RenderRequest
import com.porpoise.mandelbrot.model.Result
import com.porpoise.mandelbrot.model.ScaledView
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.SetStateRequest
import com.porpoise.mandelbrot.model.Size
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.model.TranslateXRequest
import com.porpoise.mandelbrot.model.TranslateYRequest
import com.porpoise.mandelbrot.model.UpdateRequest
import com.porpoise.mandelbrot.model.ZoomRequest
import com.porpoise.mandelbrot.Constants._
import com.porpoise.mandelbrot.render.CharacterMap
import com.porpoise.mandelbrot.model.StopAutoPlay

/**
 * Trait which holds the render logic
 */
private trait ControllerRenderTrait { this: ControllerTrait =>

  private def instructions = {
    InputCommand.charCommands.map(command => Character.valueOf(command.id.toChar) + ":" + command).mkString(" | ")
  }
  private def makeFooter: String = {
    "%s%n%s".format(makeStats, instructions)
  }
  private def makeStats = "x:%s%% | y:%s%% | zoom:%s%%".format(translateXPercentage, translateYPercentage, zoomPercentage)

  override protected def resultToString(results: Seq[Result]): String = {
    val mandelbrotText = CharacterMap.formatResults(results)
    val textAndFooter = "%s%n%s".format(mandelbrotText, makeFooter)
    textAndFooter
  }
}

trait ControllerTrait { this: Actor =>

  def mandelbrotActor: Actor
  def renderActor: Actor

  protected var currentScaledView: ScaledView = _
  protected var currentSize: Size = _
  protected var currentDepth: Int = _

  protected var translateXPercentage: Percentage = 0
  protected var translateYPercentage: Percentage = 0
  protected var zoomPercentage: Percentage = 100

  protected def resultToString(results: Seq[Result]): String

  private def notifyUpdate = mandelbrotActor ! SetAbsoluteViewRequest(currentScaledView, currentSize, currentDepth)

  private def onComputeMandelbrotRequest(newView: ScaledView, newSize: Size, newDepth: Int) = {
    currentScaledView = newView
    currentSize = newSize
    currentDepth = newDepth
    notifyUpdate
  }

  private def calculateNewView: ScaledView = {
    def percentageAsDecimal(p: Int): N = p / 100.0

    var view = currentScaledView

    if (translateXPercentage != 0) {
      view = view.translateX(percentageAsDecimal(translateXPercentage))
    }
    if (translateYPercentage != 0) {
      view = view.translateY(percentageAsDecimal(translateYPercentage))
    }
    if (zoomPercentage != 100) {
      view = view.zoom(percentageAsDecimal(zoomPercentage))
    }
    view
  }

  private def onUpdateView {

    val oldView = currentScaledView
    currentScaledView = calculateNewView

    if (oldView != currentScaledView) {
      notifyUpdate
    }
  }

  def controllerHandlers: PartialFunction[Any, Unit] = {

    // TODO - maintain a map between requests and the time sent
    case SetAbsoluteViewRequest(view, size, depth) => onComputeMandelbrotRequest(view, size, depth)

    case TranslateXRequest(percentage) =>
      translateXPercentage += percentage
      onUpdateView

    case TranslateYRequest(percentage) =>
      translateYPercentage += percentage
      onUpdateView

    case ZoomRequest(percentage) =>
      zoomPercentage += percentage
      onUpdateView

    case UpdateRequest() => onUpdateView

    case GetStateRequest() => reply(GetStateResponse(currentScaledView, currentSize, translateXPercentage, translateYPercentage, zoomPercentage))

    case SetStateRequest(xPercent, yPercent, zoom) => {
      translateXPercentage = xPercent
      translateYPercentage = yPercent
      zoomPercentage = zoom
      onUpdateView
    }

    // TODO - decorate the result with controls and timings, sending a RenderRequest instead
    case r @ MandelbrotResult(req, result) => {
      val textAndFooter = resultToString(result)
      val renderRequest = RenderRequest(textAndFooter)
      renderActor ! renderRequest
    }
  }

}
/** keep access private so it can only be interacted with via messages */
private class ControllerActor(val mandelbrotActor: Actor, val renderActor: Actor) extends Actor
  with ControllerTrait
  with ControllerRenderTrait
  with TimedUpdate
  with StoppableActor {

  currentScaledView = ScaledView.DefaultView
  currentSize = Size.DefaultSize
  currentDepth = 1000

  def act() = {
    loopWhile(running) {
      react(controllerHandlers orElse autoPlayHandler orElse stopHandler)
    }
  }
  override def onStop() = {
    // in case we're auto-zooming, stop
    stopAutoPlay
    mandelbrotActor ! Stop()
    renderActor ! Stop()
  }
}
object ControllerActor {
  def apply(mandelbrotActor: Actor, renderActor: Actor): Actor = {
    val actor = new ControllerActor(mandelbrotActor, renderActor)
    actor.start
    actor
  }
}