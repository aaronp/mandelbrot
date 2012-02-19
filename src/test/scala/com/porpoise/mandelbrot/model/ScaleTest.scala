package com.porpoise.mandelbrot.model

import org.junit.Assert
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class ScaleTest extends BaseSpec {

  describe("Scale.zoom to the centre") {

    val centre = 0.5
    it("should be able to zoom -20%") {
      val percentage = -0.2
      val (x, y) = Scale.zoom(0 -> 100, percentage)
      assertEquals(-10, x)
      assertEquals(100, y)
    }
    it("should be able to zoom 20%") {
      val percentage = 0.2
      val (x, y) = Scale.zoom(0 -> 100, percentage)
      assertEquals(10, x)
      assertEquals(90, y)
    }
    it("should be able to zoom -100%") {
      val percentage = -1.0
      val (x, y) = Scale.zoom(0 -> 100, percentage)
      assertEquals(-50, x)
      assertEquals(150, y)
    }
  }
  describe("Scale.translate") {

    it("should be able to translate a range as a percentage of the range's size") {
      Assert.assertEquals((-10, 0), Scale.translate(0, 10, -1.0))
      Assert.assertEquals((-1, 9), Scale.translate(0, 10, -0.1))
      Assert.assertEquals((10, 20), Scale.translate(0, 10, 1.0))
      Assert.assertEquals((1, 11), Scale.translate(0, 10, 0.1))

      Assert.assertEquals((2, 3), Scale.translate(1, 2, 1.0))
      Assert.assertEquals((0, 1), Scale.translate(1, 2, -1.0))

      Assert.assertEquals((100, 200), Scale.translate(0, 100, 1.0))
      Assert.assertEquals((-100, 0), Scale.translate(0, 100, -1.0))
      Assert.assertEquals((50.0, 150.0), Scale.translate(0.0, 100.0, 0.5))
    }
  }
  describe("Scale.mapRange") {
    it("should be able to scale the range -10 to 10 as a percentage") {
      val negativeTenToTenAsPercentage = Scale.mapRange(-10)(10)(0.0)(100) _
      assertEquals(0, negativeTenToTenAsPercentage(-10))
      assertEquals(100, negativeTenToTenAsPercentage(10))
      assertEquals(50, negativeTenToTenAsPercentage(0))
      assertEquals(25, negativeTenToTenAsPercentage(-5))
      assertEquals(75, negativeTenToTenAsPercentage(5))
    }
  }
}
