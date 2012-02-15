package com.porpoise.mandelbrot.actors
import com.porpoise.mandelbrot.model.Stop

trait StoppableActor {
  protected var running = true
  val stopHandler: PartialFunction[Any, Unit] = {
    case Stop() => running = false
  }
}