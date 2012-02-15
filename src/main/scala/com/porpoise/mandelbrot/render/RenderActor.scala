package com.porpoise.mandelbrot.render
import scala.actors.Actor
import com.porpoise.mandelbrot.actors.StoppableActor
import com.porpoise.mandelbrot.model.RenderRequest

class RenderActor extends Actor with StoppableActor {

  def act() = {
    loopWhile(running) {
      react(handleRequests orElse stopHandler)
    }
  }

  def handleRequests: PartialFunction[Any, Unit] = {
    case RenderRequest(results) =>
      val string = CharacterMap.formatResults(results)
      println(string)

  }
}