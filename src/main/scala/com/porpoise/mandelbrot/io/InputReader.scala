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
      case other => {
        println("read unknown dir '%s'".format(chars))
        None
      }
    }

    val charRead = in.read()
    val commandOpt = charRead match {
      //if (in.available() == 2)
      case 27 => toDir(in.read() -> in.read())
      case 32 => Some(InputCommand.Space)
      case 43 => Some(InputCommand.Plus)
      case 95 => Some(InputCommand.Minus)
      case 91 => {
        println("NINETY ONE")
        toDir(91 -> in.read())
      }
      case other => {
        println("read unknown '%s' with '%s' available".format(other, in.available()))
        None
      }
    }
    println("%s => %s".format(charRead, commandOpt))
    commandOpt
  }
}