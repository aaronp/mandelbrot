package com.porpoise.mandelbrot.model
import com.porpoise.mandelbrot.N

case class Scale(location: Double, percentage: Double) {
  require(location >= 0.0)
  require(location <= 1.0)
  def apply(coords: (N, N)) = Scale.zoom(coords, this)
}

object Scale {

  def mapRange(min: N)(max: N)(newMin: N)(newMax: N)(value: N) = {
    val diff = max - min
    val newDiff = newMax - newMin
    newMin + (value - min) / diff * newDiff
  }

  /**
   * given some coordinates, 'zoom' in on a part of those coordinates
   * using the given scale.
   *
   * e.g. if the range XY is (-10.0, 10.0) and we're going to adjust the range
   * by:
   * 
   * 90% on the left  side => zoom(XY, Scale(0.0, 0.9)) => (-10.0, 8.0)
   * 90% on the right side => zoom(XY, Scale(1.0, 0.9)) => (-8.0, 10.0)
   * 90% on in the middle  => zoom(XY, Scale(0.5, 0.9)) => (-9.0, 9.0)
   * 
   */
  def zoom(coords: (N, N), scale: Scale) : (N,N) = {
    val oldRange = coords._2 - coords._1
    val newRange = oldRange * scale.percentage
    val diff = oldRange - newRange
    val offset = diff * scale.location
    val newLeft = coords._1 + offset
    val newRight = newLeft + newRange
    (newLeft, newRight)
  }
}