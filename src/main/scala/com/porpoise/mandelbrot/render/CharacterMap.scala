package com.porpoise.mandelbrot.render

import com.porpoise.mandelbrot._
import com.porpoise.mandelbrot.model.Result
import com.porpoise.mandelbrot.model.Coords
import com.porpoise.mandelbrot.model.Scale

class CharacterMap(min: Int, max: Int) {

  private val red = c(31) _
  private val green = c(32) _
  private val yellow = c(33) _
  private val blue = c(34) _
  private def black(s: String) = blue(s) //(s: String) => s

  private def c(color: Int)(s: String) = "%c[1;%sm%s".format(27, color, s)

  val sections = ".-xX*@ ".toList.map(x => red(x.toString))

  val rangeMap = Scale.mapRange(min)(max)(0)(sections.size - 1) _

  def apply(c: Color) = {
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