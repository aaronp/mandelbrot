package com.porpoise.mandelbrot
import java.io.InputStream

import com.porpoise.mandelbrot.controller.ControllerActor
import com.porpoise.mandelbrot.model.MandelbrotActor
import com.porpoise.mandelbrot.model.Size
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.render.RenderActor
import com.porpoise.mandelbrot.render.Renderer

import Constants._

/**
 * The configuration (Config) contains the common declaration for the common actors:
 *
 * 1: The Mandelbrot Actor (s) which do the computations
 * 2: The Controller Actor which holds the state and interprets commands
 * 3: The Render Actor which does the output IO
 *
 * It also exposes a 'stop' convenience method for shutting down these actors
 */
trait Config {

  val in: InputStream
  val adjustment: Percentage = 1
  val refreshRateInMillis = 100
  val defaultResolution: Size = Size.DefaultSize
  val resolution: Size = Size(160, 50)

  def useColor: Boolean = System.getProperty("os.name").contains("Mac")

  val renderer: Renderer = if (useColor) Renderer.colored else Renderer.simple

  val mandelbrot = MandelbrotActor()

  val controller = ControllerActor(mandelbrot, renderer, RenderActor(renderer))

  private def stop = controller ! Stop()
}

object Config {

  /**
   * Given an input stream, a configuration will be initialized and passed to the given 'run' function
   */
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