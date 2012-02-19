package com.porpoise.mandelbrot.model
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import com.porpoise.mandelbrot.N
import org.junit.Assert

class BaseSpec extends Spec with ShouldMatchers {

  private val uncertainty = 0.0001

  def assertEquals(expected: N, actual: N) = Assert.assertEquals(expected, actual, uncertainty)
}