package com.porpoise.mandelbrot
import java.io.InputStream

import scala.Option.option2Iterable

import com.porpoise.mandelbrot.io._
import com.porpoise.mandelbrot.model.GetStateRequest
import com.porpoise.mandelbrot.model.MandelbrotRequest
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.SetStateRequest
import com.porpoise.mandelbrot.model.{ StartAutoPlay => Play }
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.model.{ StopAutoPlay => Pause }
import com.porpoise.mandelbrot.model.TranslateXRequest
import com.porpoise.mandelbrot.model.TranslateYRequest
import com.porpoise.mandelbrot.model.UpdateRequest
import com.porpoise.mandelbrot.model.ZoomRequest

object Main {
  import InputCommand._

  def readLoop(config: Config)(onMessage: MandelbrotRequest => Boolean) = {
    import config._

    var reading = true
    while (reading) {
      val commandOpt = InputReader.readInput(in)

      val msgOpt: Option[MandelbrotRequest] = commandOpt.map(_ match {
        case Up => TranslateYRequest(-1 * adjustment)
        case Down => TranslateYRequest(adjustment)
        case Left => TranslateXRequest(-1 * adjustment)
        case Right => TranslateXRequest(adjustment)
        case Space => SetAbsoluteViewRequest(size = resolution)
        case Plus => ZoomRequest(-1 * adjustment)
        case Minus => ZoomRequest(adjustment)
        case Minus => ZoomRequest(-1 * adjustment)
        case Quit => Stop()
        case GetState => GetStateRequest()
        case Reset => SetStateRequest()
        case StartAutoPlay => Play(refreshRateInMillis)
        case StopAutoPlay => Pause()
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

      // start by rendering the initial view
      controller ! SetAbsoluteViewRequest(size = resolution)

      readLoop(config) { msg =>
        controller ! msg
        msg != Stop()
      }
    }

    println("Done")
  }

}