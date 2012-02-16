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

  def zoom(coords: (N, N), scale: Scale) = {
    val oldRange = coords._2 - coords._1
    val newRange = oldRange * scale.percentage
    val diff = oldRange - newRange
    val offset = diff * scale.location
    val newLeft = coords._1 + offset
    val newRight = newLeft + newRange
    (newLeft, newRight, oldRange, newRange, diff, offset)
  }
}