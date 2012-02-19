package com.porpoise.mandelbrot.model

import com.porpoise.mandelbrot._
import ScaledView._
import Size._

sealed trait MandelbrotRequest
sealed trait MandelbrotResponse

/**
 * Stop request used to stop Stoppable Actors
 */
case class Stop() extends MandelbrotRequest

/**
 * Request to compute the mandelbrot series for a given, view, size and depth
 */
trait ComputeMandelbrotRequest extends MandelbrotRequest {
  def scaledView: ScaledView
  def size: Size
  def depth: Int
}

object ComputeMandelbrotRequest {
  def apply(scaledView: ScaledView, size: Size, depth: Int): ComputeMandelbrotRequest = SetAbsoluteViewRequest(scaledView, size, depth)
  def unapply(req: ComputeMandelbrotRequest) = Some(req.scaledView, req.size, req.depth)
}

/**
 * The result of a calculate mandelbrot request
 */
case class MandelbrotResult(request: ComputeMandelbrotRequest, results: Seq[Result]) extends MandelbrotResponse

/**
 * An adjust view request is a control message used to move/zoom the view
 */
sealed trait MandelbrotAdjustRequest extends MandelbrotRequest
case class TranslateXRequest(percentage: N) extends MandelbrotAdjustRequest
case class TranslateYRequest(percentage: N) extends MandelbrotAdjustRequest
case class ZoomRequest(percentage: N) extends MandelbrotAdjustRequest

/**
 * Set the absolute view to the given scale, size and depth
 */
case class SetAbsoluteViewRequest(
  scaledView: ScaledView = DefaultView,
  size: Size = DefaultSize,
  depth: Int = 1000) extends MandelbrotRequest with ComputeMandelbrotRequest

/**
 * The request sent to the renderer
 */
case class RenderRequest(text: String) extends MandelbrotRequest

/**
 * Representation of a single coordinate
 */
case class Result(pixelCoords: Coords, scaledCoords: ScaledCoords, result: Color)