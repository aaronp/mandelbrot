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

object Main {

  def readAbsoluteInput(io: IO): Option[String] = {
    val results = for {
      x1 <- io.readInt("From X: ", 0)
      y1 <- io.readInt("From Y: ", 0)
      fromXY = Coords(x1, y1)
      x2 <- io.readInt("To X: ", 200)
      y2 <- io.readInt("To Y: ", 100)
      toXY = Coords(x2, y2)
      scaleX1 <- io.readDouble("Scale X1 (between -2.5 and 1) : ", -2.5)
      scaleX2 <- io.readDouble("Scale X2 (between %s and 1) : ".format(scaleX1), 1)
      scaleX = ScaledCoords(scaleX1, scaleX2)
      scaleY1 <- io.readDouble("Scale Y1 (between -1 and 1) : ", -1)
      scaleY2 <- io.readDouble("Scale Y2 (between %s and 1) : ".format(scaleY1), 1)
      scaleY = ScaledCoords(scaleY1, scaleY2)

    } yield {
      val depth = 1000
      //val results = Mandelbrot.mapCoords(fromXY, toXY, scaleX, scaleY, depth)
      //CharacterMap.formatResults(results)
    }
    //results.headOption
    None
  }

  def readInput(io: IO): Option[MandelbrotRequest] = {
    val next = for {
      x1 <- io.readInt("Next: ", 0)
    } yield x1
    next match {
      case Some(_) => Some(SetAbsoluteViewRequest())
      case None => None
    }
  }

  def readLoop(io: IO)(f: MandelbrotRequest => Unit) = {
    var resultOpt: Option[MandelbrotRequest] = readInput(io)
    while (resultOpt.isDefined) {
      f(resultOpt.get)
      //      println(resultOpt.get)
      //      println("enter 'quit' (or 'exit', or anything that's not a number) to quit")
      resultOpt = readInput(io)
    }
  }

  def main(args: Array[String]) = {
    Config.withConfig(System.in) { config =>
      import config._

      readLoop(io) { msg =>
        println("main sending controller " + msg)
        controller ! msg
      }
    }

    println("Done")
  }

}