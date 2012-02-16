package com.porpoise.mandelbrot.model
import scala.actors.Actor
import com.porpoise.mandelbrot.actors.StoppableActor

trait MandelbrotTrait {

  /**
   * The actor to whom results should be sent
   */
  def resultActor: Actor

  def handlerMandelbrotRequest: PartialFunction[Any, Unit] = {
    case ComputeMandelbrotRequest(view, size, depth) =>
      println("mandelbrot computing for " + view)
      val results: Seq[Result] = Mandelbrot.mapCoords(size, view, depth)
      resultActor ! MandelbrotResult(results)
    case SetAbsoluteViewRequest(view, size, depth) =>
      println("mandelbrot computing for set view " + view)
      val results: Seq[Result] = Mandelbrot.mapCoords(size, view, depth)
      resultActor ! MandelbrotResult(results)
  }
}
/** keep access private so it can only be interacted with via messages */
private class MandelbrotActor(val resultActor: Actor) extends Actor
  with MandelbrotTrait with StoppableActor {

  override def onStop() = resultActor ! Stop()

  def act() = {
    loopWhile(running) {
      react(handlerMandelbrotRequest orElse stopHandler)
    }
  }
}
object MandelbrotActor {
  def apply(resultActor: Actor): Actor = {
    val actor = new MandelbrotActor(resultActor)
    actor.start
    actor
  }
}