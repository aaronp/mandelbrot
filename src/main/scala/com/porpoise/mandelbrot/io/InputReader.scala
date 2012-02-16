package com.porpoise.mandelbrot.io

import com.porpoise.mandelbrot.model.MandelbrotRequest
import java.io.InputStream

import InputCommand._
object InputReader {

  def readInput(in: InputStream): Option[InputCommand] = {
    val firstRead = in.read()
    var available = in.available()

    def toDir(chars: (Int, Int)) = chars match {
      case (91, 65) => Some(InputCommand.Up)
      case (91, 66) => Some(InputCommand.Down)
      case (91, 67) => Some(InputCommand.Right)
      case (91, 68) => Some(InputCommand.Left)
      case _ => None
    }

    in.read() match {
      case 27 if (in.available() == 2) => toDir(in.read() -> in.read())
      case 32 => Some(InputCommand.Space)
      case 32 => Some(InputCommand.Plus)
      case 32 => Some(InputCommand.Minus)
      case _ => None
    }
  }
}