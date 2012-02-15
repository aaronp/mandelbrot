package com.porpoise.mandelbrot

object Mandelbrot {

  def calculateDepth(xOffset: N, yOffset: N, depth: Int) = {

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

  def mapCoords(fromXY: (Int, Int), toXY: (Int, Int), rangeX: (N, N), rangeY: (N, N), depth: Int): Seq[Result] = {

    val (x1, y1) = fromXY
    val (x2, y2) = toXY
    val (xMin, xMax) = rangeX
    val (yMin, yMax) = rangeY
    val scaleX = Scale.mapRange(x1)(x2)(xMin)(xMax)_
    val scaleY = Scale.mapRange(y1)(y2)(yMin)(yMax)_
    for {
      coordY <- y1 to y2
      coordX <- x1 to x2
      x = scaleX(coordX)
      y = scaleY(coordY)
    } yield Result(coordX -> coordY, x -> y, Mandelbrot.calculateDepth(x, y, depth))

  }

}

