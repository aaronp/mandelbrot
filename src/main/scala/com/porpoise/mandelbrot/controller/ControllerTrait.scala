package com.porpoise.mandelbrot.controller

import com.porpoise.mandelbrot.model.MandelbrotRequest
import com.porpoise.mandelbrot.model.MandelbrotResponse
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.ScaledView
import com.porpoise.mandelbrot.model.Size
import com.porpoise.mandelbrot.model.Mandelbrot
import com.porpoise.mandelbrot.model.Result
import scala.actors.Actor
import com.porpoise.mandelbrot.model.RenderRequest

trait ControllerTrait {

  def rendererActor: Actor

  def controllerHandlers: PartialFunction[Any, Unit] = {
    case SetAbsoluteViewRequest(view, size, depth) =>
      println("controller handling " + view)
      val results: Seq[Result] = Mandelbrot.mapCoords(size, view, depth)
      rendererActor ! RenderRequest(results)
  }

}
