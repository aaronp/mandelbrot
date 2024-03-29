package com.porpoise.mandelbrot.render

import com.porpoise.mandelbrot.Constants.Color
import com.porpoise.mandelbrot.Constants.NewLine
import com.porpoise.mandelbrot.model.Coords
import com.porpoise.mandelbrot.model.Result
import com.porpoise.mandelbrot.model.Scale
import com.porpoise.mandelbrot.Constants.N

class CharacterMap(characters: String, min: Int, max: Int) {

  private def c(color: Int)(s: String) = "%c[1;%sm%s".format(27, color, s)
  private val red = c(31) _
  private val green = c(32) _
  private val yellow = c(33) _
  private val blue = c(34) _
  private val something = c(35) _
  private val somethingElse = c(36) _
  private val wot = c(37) _
  private def colors: Seq[Function1[String, String]] = Seq(something, somethingElse, wot, red, blue, green, yellow, blue)

  val sections = characters.toList.zip(colors).map { case (letter, color) => color(letter.toString) }

  val rangeMap = Scale.mapRange(min)(max)(0)(sections.size - 1) _

  def apply(c: Color): String = {
    val index = rangeMap(c).toInt
    sections(index)
  }
}
object CharacterMap {

  def formatWithCharacters(characters: String = ".-xX*@ ", results: Seq[Result]): String = {
    val charMap = characters.view.toList.map(_.toString)
    val (min, max) = minMax(results)
    val charIndexForColor = Scale.mapRange(min)(max)(0)(characters.size - 1) _
    val rangeMap: Color => Int = (c: Color) => (charIndexForColor(c)).toInt
    val colorToString: Color => String = (c: Color) => {
      val index: Int = rangeMap(c)
      charMap(index)
    }
    resultsToString(results)(colorToString)
  }

  def formatColoredResults(results: Seq[Result]): String = {
    val (min, max) = minMax(results)
    val charMap = new CharacterMap(".-xX*@ ", min, max)
    resultsToString(results)(charMap.apply _)
  }

  private def minMax(results: Seq[Result]) = {
    val colors = results map { _.result }
    colors.min -> colors.max
  }

  private def resultsToString(results: Seq[Result])(charMap: Color => String): String = {
    val buffer = new StringBuilder

    var lastY = results.head.pixelCoords.x
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