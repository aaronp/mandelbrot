package com.porpoise.mandelbrot
import java.io.InputStream
import com.porpoise.mandelbrot.controller.ControllerActor
import com.porpoise.mandelbrot.render.RenderActor

class Config(val io: IO) {
  val renderer = new RenderActor()
  renderer.start
  val controller = new ControllerActor(renderer)

  controller.start
}

object Config {
  def apply(in: InputStream): Config = new Config(new IO(in))
}