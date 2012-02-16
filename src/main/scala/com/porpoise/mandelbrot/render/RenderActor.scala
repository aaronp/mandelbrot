package com.porpoise.mandelbrot.render
import scala.actors.Actor
import com.porpoise.mandelbrot.actors.StoppableActor
import com.porpoise.mandelbrot.model.MandelbrotResult

trait RenderTrait {

  def handleRequests: PartialFunction[Any, Unit] = {
    case MandelbrotResult(results) =>
      println("rendering ")
      val string = CharacterMap.formatResults(results)
      println(string)

  }
}

private class RenderActor extends Actor with RenderTrait with StoppableActor {

  def act() = {
    loopWhile(running) {
      react(handleRequests orElse stopHandler)
    }
  }

}
object RenderActor {
  def apply(): Actor = {
    val actor = new RenderActor()
    actor.start
    actor
  }
}