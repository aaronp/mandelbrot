package com.porpoise.mandelbrot.model

import org.junit.Assert
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class ScaleTest extends Spec with ShouldMatchers {

  val uncertainty = 0.0001

  val extractedLocalValue = Scale.translate((0, 10), 1.0)

  describe("Scale.translate") {

    it("should be able to translate a range as a percentage of the range's size") {
      Assert.assertEquals((-1, 9), Scale.translate((0, 10), -1.0))
      Assert.assertEquals((1, 11), Scale.translate((0, 10), 1.0))

    }
  }
  describe("Scale.mapRange") {
    it("should be able to scale the range -10 to 10 as a percentage") {
      val negativeTenToTenAsPercentage = Scale.mapRange(-10)(10)(0.0)(100) _
      Assert.assertEquals(0, negativeTenToTenAsPercentage(-10), uncertainty)
      Assert.assertEquals(100, negativeTenToTenAsPercentage(10), uncertainty)
      Assert.assertEquals(50, negativeTenToTenAsPercentage(0), uncertainty)
      Assert.assertEquals(25, negativeTenToTenAsPercentage(-5), uncertainty)
      Assert.assertEquals(75, negativeTenToTenAsPercentage(5), uncertainty)
    }
  }
}
