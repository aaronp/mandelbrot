package com.porpoise.mandelbrot.controller
import scala.actors.Actor
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.actors.StoppableActor
import com.porpoise.mandelbrot.model.Mandelbrot
import com.porpoise.mandelbrot.model.MandelbrotResult
import com.porpoise.mandelbrot.model.Result
import com.porpoise.mandelbrot.model.AdjustViewRequest
import com.porpoise.mandelbrot.model.ScaledView
import com.porpoise.mandelbrot.model.Size
import com.porpoise.mandelbrot.model.ComputeMandelbrotRequest
import com.porpoise.mandelbrot.model.Scale
import com.porpoise.mandelbrot.N

trait ControllerTrait {

  def mandelbrotActor: Actor
  def renderActor: Actor

  protected var currentScaledView: ScaledView = _
  protected var currentSize: Size = _
  protected var currentDepth: Int = _

  private def notifyUpdate = mandelbrotActor ! SetAbsoluteViewRequest(currentScaledView, currentSize, currentDepth)

  private def onComputeMandelbrotRequest(newView: ScaledView, newSize: Size, newDepth: Int) = {
    currentScaledView = newView
    currentSize = newSize
    currentDepth = newDepth
    notifyUpdate
  }

  private def onAdjustView(xDelta: N, yDelta: N, zoom: N) = {
    currentScaledView = currentScaledView.adjust(xDelta, yDelta, zoom)
    notifyUpdate
  }

  def controllerHandlers: PartialFunction[Any, Unit] = {

    // TODO - maintain a map between requests and the time sent
    case SetAbsoluteViewRequest(view, size, depth) => onComputeMandelbrotRequest(view, size, depth)

    case AdjustViewRequest(xDelta, yDelta, zoomPercent) => onAdjustView(xDelta, yDelta, zoomPercent)

    // TODO - decorate the result with controls and timings, sending a RenderRequest instead
    case r @ MandelbrotResult(req, result) => renderActor.forward(r)
  }

}
/** keep access private so it can only be interacted with via messages */
private class ControllerActor(val mandelbrotActor: Actor, val renderActor: Actor) extends Actor
  with ControllerTrait with StoppableActor {

  currentScaledView = ScaledView.DefaultView
  currentSize = Size.DefaultSize
  currentDepth = 1000

  def act() = {
    loopWhile(running) {
      react(controllerHandlers orElse stopHandler)
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