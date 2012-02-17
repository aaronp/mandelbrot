package com.porpoise.mandelbrot.render

import com.porpoise.mandelbrot._
import com.porpoise.mandelbrot.model.Result
import com.porpoise.mandelbrot.model.Coords
import com.porpoise.mandelbrot.model.Scale

class CharacterMap(min: Int, max: Int) {
  val sections: Seq[Char] = Seq('.', '-', 'x', 'X', '*', '@', '!', ' ')

  val rangeMap = Scale.mapRange(min)(max)(0)(sections.size - 1) _

  def apply(c: Color): Char = {
    val index = rangeMap(c).toInt
    sections(index)
  }
}
object CharacterMap {

  def formatResults(results: Seq[Result]): String = {

    val colors = results map { _.result }
    val min = colors.min
    val max = colors.max
    val charMap = new CharacterMap(min, max)

    var lastY = results.head.pixelCoords.x
    val buffer = new StringBuilder

    for (Result(Coords(x, y), _, color) <- results) {
      val character = charMap(color)
      buffer.append(character)
      if (y != lastY) {
        lastY = y
        buffer.append(NewLine)
      }
    }
    buffer.toString
  }

}