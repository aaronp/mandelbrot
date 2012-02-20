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
case class TranslateXRequest(percentage: Percentage) extends MandelbrotAdjustRequest
case class TranslateYRequest(percentage: Percentage) extends MandelbrotAdjustRequest
case class ZoomRequest(percentage: Percentage) extends MandelbrotAdjustRequest

/** Forces a redraw w/o any other change screen */
case class UpdateRequest() extends MandelbrotRequest

case class GetStateRequest() extends MandelbrotRequest
case class GetStateResponse(scaledView: ScaledView, resolution: Size, translateXPercentage: Percentage, translateYPercentage: Percentage, zoomPercentage: Percentage) extends MandelbrotResponse

case class SetStateRequest(
  translateXPercentage: Percentage = 0,
  translateYPercentage: Percentage = 0,
  zoomPercentage: Percentage = 100) extends MandelbrotRequest

/**
 * start auto-play
 */
case class StartAutoPlay(delayInMillis: Int = 2000) extends MandelbrotRequest
case class StopAutoPlay() extends MandelbrotRequest

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