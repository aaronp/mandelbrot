package com.porpoise.mandelbrot.render
import scala.actors.Actor
import com.porpoise.mandelbrot.actors.StoppableActor
import com.porpoise.mandelbrot.model.MandelbrotResult
import com.porpoise.mandelbrot.model.RenderRequest
import com.porpoise.mandelbrot.model.Result

trait Renderer {
  def formatResults(results: Seq[Result]): String = CharacterMap.formatWithCharacters(results = results)
}
object Renderer {
  lazy val colored = new Renderer {
    override def formatResults(results: Seq[Result]): String = CharacterMap.formatColoredResults(results)
  }
  lazy val simple = new Renderer {}
}

trait RenderTrait {

  def renderer: Renderer

  def handleRequests: PartialFunction[Any, Unit] = {
    case MandelbrotResult(_, results) => Actor.self ! RenderRequest(renderer.formatResults(results))
    case RenderRequest(text) => println(text)
  }
}

private class RenderActor(val renderer: Renderer) extends Actor with RenderTrait with StoppableActor {
  def act() = {
    loopWhile(running) {
      react(handleRequests orElse stopHandler)
    }
  }
}
object RenderActor {
  def apply(renderer: Renderer): Actor = {
    val actor = new RenderActor(renderer)
    actor.start
    actor
  }
}