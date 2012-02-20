package com.porpoise.mandelbrot.io

import com.porpoise.mandelbrot.model.MandelbrotRequest
import java.io.InputStream

import InputCommand._
object InputReader {

  private val InputCommandValueIds = InputCommand.values.map(v => v.id)

  def readInput(in: InputStream): Option[InputCommand] = {

    def toDir(chars: (Int, Int)) = chars match {
      case (91, InputCommand.UpId) => Some(InputCommand.Up)
      case (91, InputCommand.DownId) => Some(InputCommand.Down)
      case (91, InputCommand.RightId) => Some(InputCommand.Right)
      case (91, InputCommand.LeftId) => Some(InputCommand.Left)
      case other => {
        println("read unknown direction '%s'".format(chars))
        None
      }
    }

    val charRead = in.read()
    var available = in.available()

    val commandOpt = charRead match {
      case 27 if (in.available() == 2) => toDir(in.read() -> in.read())
      case 91 if (in.available() == 1) => {
        toDir(91 -> in.read())
        throw new IllegalStateException("HACK! Accepting 91 when 1 available!")
      }
      case x if (InputCommandValueIds.contains(x)) => Some(InputCommand.apply(x))
      case other => {
        println("read unknown '%s' with '%s' available".format(other, in.available()))
        None
      }
    }
    commandOpt
  }
}