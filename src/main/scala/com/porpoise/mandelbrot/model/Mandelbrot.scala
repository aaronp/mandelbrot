package com.porpoise.mandelbrot.model

import com.porpoise.mandelbrot.N
import com.porpoise.mandelbrot.Color

object Mandelbrot {

  /**
   * with the given x and y coords, calculate the given result for the given depth
   */
  def calculateDepth(coords: ScaledCoords, depth: Int): Color = {

    val ScaledCoords(xOffset: N, yOffset) = coords
    var x: N = 0
    var y: N = 0

    var iter = 0
    while (x * x + y * y < 4 && iter < depth) {
      val tempX = xOffset + (x * x - y * y)
      y = yOffset + (x * y * 2)
      x = tempX
      iter = iter + 1
    }

    iter
  }

  def mapCoords(fromXY: Coords, toXY: Coords, rangeX: ScaledCoords, rangeY: ScaledCoords, depth: Int): Seq[Result] = {

    val Coords(x1, y1) = fromXY
    val Coords(x2, y2) = toXY
    val ScaledCoords(xMin, xMax) = rangeX
    val ScaledCoords(yMin, yMax) = rangeY
    val scaleX = Scale.mapRange(x1)(x2)(xMin)(xMax)_
    val scaleY = Scale.mapRange(y1)(y2)(yMin)(yMax)_
    for {
      coordY <- y1 to y2
      coordX <- x1 to x2
      x = scaleX(coordX)
      y = scaleY(coordY)
    } yield {
      val scaled = ScaledCoords(x, y)
      Result(Coords(coordX, coordY), scaled, calculateDepth(scaled, depth))
    }

  }

}

