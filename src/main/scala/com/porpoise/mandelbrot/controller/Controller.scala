package com.porpoise.mandelbrot.controller
import scala.actors.Actor

import com.porpoise.mandelbrot.actors.StoppableActor
import com.porpoise.mandelbrot.actors.TimedActor
import com.porpoise.mandelbrot.model.ComputeMandelbrotRequest
import com.porpoise.mandelbrot.model.GetStateRequest
import com.porpoise.mandelbrot.model.GetStateResponse
import com.porpoise.mandelbrot.model.MandelbrotResult
import com.porpoise.mandelbrot.model.ScaledView
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.SetStateRequest
import com.porpoise.mandelbrot.model.Size
import com.porpoise.mandelbrot.model.StartAutoPlay
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.model.StopAutoPlay
import com.porpoise.mandelbrot.model.TranslateXRequest
import com.porpoise.mandelbrot.model.TranslateYRequest
import com.porpoise.mandelbrot.model.UpdateRequest
import com.porpoise.mandelbrot.model.ZoomRequest
import com.porpoise.mandelbrot.N
import com.porpoise.mandelbrot.Percentage

/**
 * Trait responsible for starting/stopping a timed actor
 */
trait TimedUpdate { thisActor: Actor =>
  private var timedCaller: Option[Actor] = None

  protected def newMessage: Any = UpdateRequest()

  protected def targetActor: Actor = thisActor

  private def onStart(delayInMillis: Int) = {

    def startTimer: Actor = TimedActor(targetActor, delayInMillis) { newMessage _ }

    timedCaller = timedCaller match {
      case None => Some(startTimer)
      case Some(t) => Some(t)
    }
  }

  private def onStop = {
    timedCaller foreach { t => t ! Stop() }
    timedCaller = None
  }

  def autoPlayHandler: PartialFunction[Any, Unit] = {

    case StartAutoPlay(delayInMillis) => onStart(delayInMillis)

    case stop: StopAutoPlay => onStop
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
    }

    // TODO - decorate the result with controls and timings, sending a RenderRequest instead
    case r @ MandelbrotResult(req, result) => renderActor.forward(r)
  }

}
/** keep access private so it can only be interacted with via messages */
private class ControllerActor(val mandelbrotActor: Actor, val renderActor: Actor) extends Actor
  with ControllerTrait
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