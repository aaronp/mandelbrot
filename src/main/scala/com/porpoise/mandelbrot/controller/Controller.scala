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

  def controllerHandlers: PartialFunction[Any, Unit] = {
    case r @ SetAbsoluteViewRequest(view, size, depth) =>
      println("controller forwarding " + r)
      mandelbrotActor.forward(r)
  }

}
/** keep access private so it can only be interacted with via messages */
private class ControllerActor(val mandelbrotActor: Actor) extends Actor
  with ControllerTrait with StoppableActor {

  def act() = {
    loopWhile(running) {
      react(controllerHandlers orElse stopHandler)
    }
  }
  override def onStop() = mandelbrotActor ! Stop()
}
object ControllerActor {
  def apply(mandelbrotActor: Actor): Actor = {
    val actor = new ControllerActor(mandelbrotActor)
    actor.start
    actor
  }
}