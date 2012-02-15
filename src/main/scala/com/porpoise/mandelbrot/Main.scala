package com.porpoise.mandelbrot
import scala.Option.option2Iterable

object Main {

  def runNext(io: IO): Option[String] = {
    val results = for {
      x1 <- io.readInt("From X: ", 0)
      y1 <- io.readInt("From Y: ", 0)
      fromXY = (x1, y1)
      x2 <- io.readInt("To X: ", 10)
      y2 <- io.readInt("To Y: ", 10)
      toXY = (x2, y2)
      scaleX1 <- io.readDouble("Scale X1 (between -2.5 and 1) : ", -2.5)
      scaleX2 <- io.readDouble("Scale X2 (between %s and 1) : ".format(scaleX1), 1)
      scaleX = (scaleX1, scaleX2)
      scaleY1 <- io.readDouble("Scale Y1 (between -1 and 1) : ", -1)
      scaleY2 <- io.readDouble("Scale Y2 (between %s and 1) : ".format(scaleY1), 1)
      scaleY = (scaleY1, scaleY2)

    } yield {
      val depth = 1000
      val results = Mandelbrot.mapCoords(fromXY, toXY, scaleX, scaleY, depth)
      CharacterMap.formatResults(results)
    }
    results.headOption
  }

  def main(args: Array[String]) = {

    val io = new IO(System.in)

    var resultOpt: Option[String] = runNext(io)
    while (resultOpt.isDefined) {
      println(resultOpt.get)
      println("enter 'quit' (or 'exit', or anything that's not a number) to quit")
      resultOpt = runNext(io)
    }
    println("Done")
  }

}