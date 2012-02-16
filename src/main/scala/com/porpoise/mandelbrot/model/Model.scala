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

  def adjust(xLocation: N, yLocation: N, zoom: Double): ScaledView = {
    val newX: (N, N) = Scale.zoom(xCoords, Scale(location = xLocation, percentage = zoom))
    val newY: (N, N) = Scale.zoom(yCoords, Scale(location = yLocation, percentage = zoom))
    ScaledView(newX, newY)
  }
}

object ScaledView {
  val DefaultView = ScaledView()
  def apply(xRange: (N, N), yRange: (N, N)): ScaledView = ScaledView(
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

