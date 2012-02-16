package com.porpoise.mandelbrot.model

import com.porpoise.mandelbrot._
import ScaledView._
import Size._

sealed trait MandelbrotRequest
sealed trait MandelbrotResponse

case class Stop() extends MandelbrotRequest

trait ComputeMandelbrotRequest extends MandelbrotRequest {
  def scaledView: ScaledView
  def size: Size
  def depth: Int
}
object ComputeMandelbrotRequest {
  def apply(scaledView: ScaledView, size: Size, depth: Int): ComputeMandelbrotRequest = SetAbsoluteViewRequest(scaledView, size, depth)
  def unapply(req: ComputeMandelbrotRequest) = Some(req.scaledView, req.size, req.depth)
}

case class SetAbsoluteViewRequest(
  scaledView: ScaledView = DefaultView,
  size: Size = DefaultSize,
  depth: Int = 1000) extends MandelbrotRequest with ComputeMandelbrotRequest

case class MandelbrotResult(results: Seq[Result]) extends MandelbrotResponse

/**
 * Representation of a single coordinate
 */
case class Result(pixelCoords: Coords, scaledCoords: ScaledCoords, result: Color)