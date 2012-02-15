package com.porpoise.mandelbrot.model
import com.porpoise.mandelbrot.N

object Scale {

  def mapRange(min: N)(max: N)(newMin: N)(newMax: N)(value: N) = {
    val diff = max - min
    val newDiff = newMax - newMin
    newMin + (value - min) / diff * newDiff
  }
}