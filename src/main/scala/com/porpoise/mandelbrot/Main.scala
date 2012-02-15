package com.porpoise.mandelbrot
import java.io.InputStream

import scala.Option.option2Iterable

import com.porpoise.mandelbrot.controller.ControllerActor
import com.porpoise.mandelbrot.model.Mandelbrot

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
      val results = Mandelbrot.mapCoords(fromXY, toXY, scaleX, scaleY, depth)
      CharacterMap.formatResults(results)
    }
    results.headOption
  }

  def readInput(io: IO): Option[MandelbrotRequest] = {
    None
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

    val io = new IO(System.in)

    val controller = new ControllerActor()
    controller.start

    readLoop(io) { msg =>
      controller ! msg
    }

    println("Done")
  }

}