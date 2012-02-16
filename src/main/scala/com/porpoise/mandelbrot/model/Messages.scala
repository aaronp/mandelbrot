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

case class AdjustViewRequest(xDelta: N, yDelta: N, zoomPercentage: N) extends MandelbrotRequest
object AdjustViewRequest {
  def up(delta: N): AdjustViewRequest = AdjustViewRequest(0.5, 0.5 + delta, 0.90)
  def down(delta: N): AdjustViewRequest = AdjustViewRequest(0.5, 0.5 - delta, 0.90)
  def left(delta: N): AdjustViewRequest = AdjustViewRequest(0.5 - delta, 0.5, 0.90)
  def right(delta: N): AdjustViewRequest = AdjustViewRequest(0.5 + delta, 0.5, 0.90)
  def zoom(total: N): AdjustViewRequest = AdjustViewRequest(0.5, 0.5, total)
}

case class SetAbsoluteViewRequest(
  scaledView: ScaledView = DefaultView,
  size: Size = DefaultSize,
  depth: Int = 1000) extends MandelbrotRequest with ComputeMandelbrotRequest

case class MandelbrotResult(request: ComputeMandelbrotRequest, results: Seq[Result]) extends MandelbrotResponse

case class RenderRequest(text: String) extends MandelbrotRequest

/**
 * Representation of a single coordinate
 */
case class Result(pixelCoords: Coords, scaledCoords: ScaledCoords, result: Color)