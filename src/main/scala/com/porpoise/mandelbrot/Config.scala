package com.porpoise.mandelbrot
import java.io.InputStream

import com.porpoise.mandelbrot.controller.ControllerActor

class Config(val io: IO) {
  val controller = new ControllerActor()
  controller.start
}

object Config {
  def apply(in: InputStream): Config = new Config(new IO(in))
}