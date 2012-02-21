package com.porpoise.mandelbrot.model

import com.porpoise.mandelbrot.Constants._

object Scale {

  /**
   * Given the first range, map the target number into the new range
   *
   * e.g. given:
   * <pre>
   *    OLD   |    NEW   | VALUE | RESULT
   *   0 - 10 |  0 - 100 |   7   |   70.0
   * -10 - 10 | 10 - 20  |   0   |   15.0
   *  50 - 60 |  1 - 5   |  50   |   1.0
   *  50 - 60 |  1 - 5   |  60   |   5.0
   * </pre>
   */
  def mapRange[R1 <% N, R2 <% N](min: R1)(max: R1)(newMin: R2)(newMax: R2)(value: N): N = {
    val diff = max - min
    val newDiff = newMax - newMin
    newMin + (value - min) / diff * newDiff
  }

  def translate[T](x: N, y: N, percentage: N)(implicit nToT: N => T): (T, T) = {
    val newRange = (y - x) * percentage
    val newX: T = x + newRange
    val newY: T = y + newRange
    (newX, newY)
  }

  /**
   * given some coordinates, 'zoom' in on a part of those coordinates
   * using the given scale.
   *
   * e.g. if the range XY is (-10.0, 10.0) and we're going to adjust the range
   * by:
   *
   * 90% on the left  side => zoom(XY, Scale(0.0, 0.9)) => (-10.0, 8.0)
   * 90% on the right side => zoom(XY, Scale(1.0, 0.9)) => (-8.0, 10.0)
   * 90% on in the middle  => zoom(XY, Scale(0.5, 0.9)) => (-9.0, 9.0)
   *
   */
  def zoom[T <% N](coords: (T, T), percentage: N): (N, N) = {
    require(percentage >= 0.0, "percentage must be positive: %s".format(percentage))
    val oldRange = coords._2 - coords._1
    val newRange = oldRange * percentage
    val diff = oldRange - newRange
    val offset = diff * 0.5
    val newLeft = coords._1 + offset
    val newRight = newLeft + newRange
    newLeft -> newRight
  }
}