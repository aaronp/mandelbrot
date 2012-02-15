package com.porpoise.mandelbrot
import com.porpoise.mandelbrot.model.Result
import com.porpoise.mandelbrot.model.Coords

object CharacterMap {

  def charForDepth(color: Color): Char = {
    color match {
      case 1 => '.'
      case 2 => '-'
      case 3 => 'x'
      case 4 => 'X'
      case x if (x < 20) => '*'
      case x if (x < 50) => '$'
      case _ => ' '
    }
  }

  def formatResults(results: Seq[Result]): String = {
    var lastY = results.head.pixelCoords.x
    val buffer = new StringBuilder
    for (Result(Coords(x, y), _, color) <- results) {
      val character = charForDepth(color)
      buffer.append(character)
      if (y != lastY) {
        lastY = y
        buffer.append(NewLine)
      }
    }
    buffer.toString
  }

}