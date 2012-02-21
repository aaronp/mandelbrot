package com.porpoise.mandelbrot.model
import org.junit.Assert
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import com.porpoise.mandelbrot.Constants.N
import com.porpoise.mandelbrot.Constants.Percentage

class BaseSpec extends Spec with ShouldMatchers {

  private val uncertainty = 0.0001

  def assertEquals(expected: N, actual: N) = Assert.assertEquals(expected, actual, uncertainty)
}