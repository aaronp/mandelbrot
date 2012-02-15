package com.porpoise.mandelbrot.actors
import com.porpoise.mandelbrot.model.SetAbsoluteViewResponse
import com.porpoise.mandelbrot.model.Stop
import javax.crypto.Cipher.r

trait StoppableActor {
  protected var running = true
  val stopHandler: PartialFunction[Any, Unit] = {
    case Stop() => running = false
  }
}