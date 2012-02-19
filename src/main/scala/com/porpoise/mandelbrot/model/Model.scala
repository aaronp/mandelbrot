package com.porpoise.mandelbrot.model

import com.porpoise.mandelbrot._

/**
 * COORDS
 */
case class Coords(x: Int, y: Int)

/**
 * SCALED COORDS
 */
case class ScaledCoords(x: N, y: N)
object ScaledCoords {
  val TopLeft = ScaledCoords(-2.5, -1)
  val BottomRight = ScaledCoords(1, 1)
}
import ScaledCoords._

/**
 * SCALED VIEW
 */
case class ScaledView(topLeft: ScaledCoords = TopLeft, bottomRight: ScaledCoords = BottomRight) {
  require(topLeft.x <= bottomRight.x)
  require(topLeft.y <= bottomRight.y)
  lazy val x1 = topLeft.x
  lazy val y1 = topLeft.y
  lazy val x2 = bottomRight.x
  lazy val y2 = bottomRight.y

  def xCoords: (N, N) = x1 -> x2
  def yCoords: (N, N) = y1 -> y2

  def zoom(percentage: N): ScaledView = {
    var newX: (N, N) = Scale.zoom(xCoords, percentage = percentage)
    var newY: (N, N) = Scale.zoom(yCoords, percentage = percentage)
    ScaledView(newX, newY)
  }
  def translateX(xPercentage: N): ScaledView = {
    val (newX1, newX2) = Scale.translate(x1, x2, xPercentage)
    ScaledView(ScaledCoords(newX1, y1), ScaledCoords(newX2, y2))
  }
  def translateY(yPercentage: N): ScaledView = {
    val (newY1, newY2) = Scale.translate(y1, y2, yPercentage)
    ScaledView(ScaledCoords(x1, newY1), ScaledCoords(x2, newY2))
  }

  def toCoords = (x1, y1, x2, y2)
}

object ScaledView {
  val DefaultView = ScaledView()
  def apply[T <% N](xRange: (T, T), yRange: (T, T)): ScaledView = ScaledView(
    ScaledCoords(xRange._1, yRange._1),
    ScaledCoords(xRange._2, yRange._2))
}
import ScaledView._

/**
 * SIZE
 */
case class Size(width: Int, height: Int)
object Size {
  val DefaultSize = Size(120, 80)
}
import Size._

