package com.porpoise.mandelbrot.controller
import scala.actors.Actor
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.actors.StoppableActor
import com.porpoise.mandelbrot.model.Mandelbrot
import com.porpoise.mandelbrot.model.MandelbrotResult
import com.porpoise.mandelbrot.model.Result

trait ControllerTrait { 

  def mandelbrotActor: Actor
  def renderActor: Actor

  def controllerHandlers: PartialFunction[Any, Unit] = {
    // TODO - maintain a map between requests and the time sent
    case r @ SetAbsoluteViewRequest(view, size, depth) => mandelbrotActor ! r

    // TODO - decorate the result with controls and timings, sending a RenderRequest instead
    case r @ MandelbrotResult(req, result) => renderActor.forward(r)
  }

}
/** keep access private so it can only be interacted with via messages */
private class ControllerActor(val mandelbrotActor: Actor, val renderActor: Actor) extends Actor
  with ControllerTrait with StoppableActor {

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
  def apply(mandelbrotActor: Actor, renderActor : Actor): Actor = {
    val actor = new ControllerActor(mandelbrotActor, renderActor)
    actor.start
    actor
  }
}