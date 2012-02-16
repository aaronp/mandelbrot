package com.porpoise.mandelbrot
import java.io.InputStream

import com.porpoise.mandelbrot.controller.ControllerActor
import com.porpoise.mandelbrot.model.MandelbrotActor
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.render.RenderActor

trait Config {
  def io: IO

  val renderer = RenderActor()

  val mandelbrot = MandelbrotActor(renderer)

  val controller = ControllerActor(mandelbrot)

  private def stop = controller ! Stop()
}

object Config {
  def withConfig(in: InputStream)(f: Config => Unit) = {
    val config = new Config {
      val io = new IO(in)
    }

    try {
      f(config)
    } finally {
      config.stop
    }
  }
}