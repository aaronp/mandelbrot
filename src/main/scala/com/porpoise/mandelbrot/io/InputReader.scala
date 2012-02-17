package com.porpoise.mandelbrot.io

import com.porpoise.mandelbrot.model.MandelbrotRequest
import java.io.InputStream

import InputCommand._
object InputReader {

  def readInput(in: InputStream): Option[InputCommand] = {

    def toDir(chars: (Int, Int)) = chars match {
      case (91, 65) => Some(InputCommand.Up)
      case (91, 66) => Some(InputCommand.Down)
      case (91, 67) => Some(InputCommand.Right)
      case (91, 68) => Some(InputCommand.Left)
      case other => {
        println("read unknown dir '%s'".format(chars))
        None
      }
    }

    val charRead = in.read()
    var available = in.available()
    val commandOpt = charRead match {
      case 27 if (in.available() == 2) => toDir(in.read() -> in.read())
      case 32 => Some(InputCommand.Space)
      case 43 => Some(InputCommand.Plus)
      case 95 => Some(InputCommand.Minus)
      case 91 if (in.available() == 1) => toDir(91 -> in.read())
      case other => {
        println("read unknown '%s' with '%s' available".format(other, in.available()))
        None
      }
    }
    commandOpt
  }
}