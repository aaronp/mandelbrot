package com.porpoise.mandelbrot.model
import scala.actors.Actor
import com.porpoise.mandelbrot.actors.StoppableActor

trait MandelbrotTrait { this: Actor =>

  def handlerMandelbrotRequest: PartialFunction[Any, Unit] = {
    case r @ ComputeMandelbrotRequest(view, size, depth) =>
      val results: Seq[Result] = Mandelbrot.mapCoords(size, view, depth)
      reply(MandelbrotResult(r, results))
  }
}
/** keep access private so it can only be interacted with via messages */
private class MandelbrotActor extends Actor
  with MandelbrotTrait with StoppableActor {

  def act() = {
    loopWhile(running) {
      react(handlerMandelbrotRequest orElse stopHandler)
    }
  }
}
object MandelbrotActor {
  def apply(): Actor = {
    val actor = new MandelbrotActor()
    actor.start
    actor
  }
}