package com.porpoise.mandelbrot
import java.io.InputStream
import scala.Option.option2Iterable
import com.porpoise.mandelbrot.controller.ControllerActor
import com.porpoise.mandelbrot.model.Mandelbrot
import com.porpoise.mandelbrot.model.Coords
import com.porpoise.mandelbrot.model.MandelbrotRequest
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.ScaledCoords
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.model.AdjustViewRequest
import com.porpoise.mandelbrot.io._

object Main {
  import InputCommand._

  def readLoop(input: InputStream)(onMessage: MandelbrotRequest => Boolean) = {

    var reading = true
    while (reading) {
      val commandOpt = InputReader.readInput(input)

      val adjustment = 0.1
      val msgOpt: Option[MandelbrotRequest] = commandOpt.map(command => command match {
        case Up => AdjustViewRequest.up(adjustment)
        case Down => AdjustViewRequest.down(adjustment)
        case Left => AdjustViewRequest.left(adjustment)
        case Right => AdjustViewRequest.right(adjustment)
        case Space => Stop()
        case Plus => AdjustViewRequest.zoom(0.9)
        case Minus => AdjustViewRequest.zoom(1.1)
      })

      reading = msgOpt match {
        case Some(msg) => onMessage(msg)
        case None => false
      }
    }
  }

  def main(args: Array[String]) = {
    Config.withConfig(System.in) { config =>
      import config._

      controller ! SetAbsoluteViewRequest()

      readLoop(in) { msg =>
        controller ! msg
        msg != Stop()
      }
    }

    println("Done")
  }

}