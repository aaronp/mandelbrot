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

  def readLoop(input: InputStream)(onMessage: MandelbrotRequest => Boolean) = {

    var reading = true
    while (reading) {
      val commandOpt = InputReader.readInput(input)

      val adjustment: Percentage = 5
      val refreshRateInMillis = 500
      val msgOpt: Option[MandelbrotRequest] = commandOpt.map(_ match {
        case Up => TranslateYRequest(-1 * adjustment)
        case Down => TranslateYRequest(adjustment)
        case Left => TranslateXRequest(-1 * adjustment)
        case Right => TranslateXRequest(adjustment)
        case Space => UpdateRequest()
        case Plus => ZoomRequest(adjustment)
        case Minus => ZoomRequest(-1 * adjustment)
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

      controller ! SetAbsoluteViewRequest()

      readLoop(in) { msg =>
        controller ! msg
        msg != Stop()
      }
    }

    println("Done")
  }

}