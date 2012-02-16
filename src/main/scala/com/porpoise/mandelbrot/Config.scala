package com.porpoise.mandelbrot
import java.io.InputStream

import com.porpoise.mandelbrot.controller.ControllerActor
import com.porpoise.mandelbrot.model.MandelbrotActor
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.render.RenderActor

trait Config {
  val in: InputStream

  val renderer = RenderActor()

  val mandelbrot = MandelbrotActor()

  val controller = ControllerActor(mandelbrot, renderer)

  private def stop = controller ! Stop()
}

object Config {
  def withConfig(input: InputStream)(f: Config => Unit) = {
    val config = new Config {
      val in = input
    }

    try {
      f(config)
    } finally {
      config.stop
    }
  }
}