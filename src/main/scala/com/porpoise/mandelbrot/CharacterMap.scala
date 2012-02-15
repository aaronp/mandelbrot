package com.porpoise.mandelbrot

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
    var lastY = results.head.pixelCoords._2
    val buffer = new StringBuilder
    for (Result((x, y), _, color) <- results) {
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