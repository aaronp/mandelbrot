package com.porpoise.mandelbrot.model

import com.porpoise.mandelbrot._
import ScaledView._
import Size._

sealed trait MandelbrotRequest
sealed trait MandelbrotResponse

case class SetAbsoluteViewRequest(
  scaledView: ScaledView = DefaultView,
  size: Size = DefaultSize,
  depth: Int = 1000) extends MandelbrotRequest
case class SetAbsoluteViewResponse(request: SetAbsoluteViewRequest, results: Seq[Result]) extends MandelbrotResponse

/**
 * Representation of a single coordinate
 */
case class Result(pixelCoords: Coords, scaledCoords: ScaledCoords, result: Color)