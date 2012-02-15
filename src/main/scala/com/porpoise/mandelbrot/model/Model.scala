package com.porpoise.mandelbrot

sealed trait MandelbrotRequest
sealed trait MandelbrotResponse

case class ScaledCoords(x: N, y: N)
case class Coords(x: Int, y: Int)

case class ScaledView(topLeft: ScaledCoords, bottomRight: ScaledCoords) {
  require(topLeft.x <= bottomRight.x)
  require(topLeft.y <= bottomRight.y)
  lazy val x1 = topLeft.x
  lazy val y1 = topLeft.y
  lazy val x2 = bottomRight.x
  lazy val y2 = bottomRight.y
}

case class Size(width: Int, height: Int)

/**
 * Set the view to the given
 */
case class SetAbsoluteViewRequest(scaledView: ScaledView, size: Size, depth: Int = 1000) extends MandelbrotRequest
case class SetAbsoluteViewResponse(request: SetAbsoluteViewRequest, results: Seq[Result]) extends MandelbrotResponse

/**
 * Representation of a single coordinate
 */
case class Result(pixelCoords: Coords, scaledCoords: ScaledCoords, result: Color)
