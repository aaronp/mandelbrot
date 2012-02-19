package com.porpoise.mandelbrot.model

import org.junit.Assert
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class ModelTest extends Spec with ShouldMatchers {

  describe("ScaledView.translateX") {
    val original = ScaledView(0 -> 10, 100 -> 200)
    it("should be able to be translate -10%") {
      val (x1, y1, x2, y2) = original.translateX(-0.1).toCoords
      Assert.assertEquals(-1.0, x1)
      Assert.assertEquals(original.y1, y1)
      Assert.assertEquals(9.0, x2)
      Assert.assertEquals(original.y2, y2)
    }
    it("should be able to translate 10%") {
      val (x1, y1, x2, y2) = original.translateX(0.1).toCoords
      Assert.assertEquals(1.0, x1)
      Assert.assertEquals(original.y1, y1)
      Assert.assertEquals(11.0, x2)
      Assert.assertEquals(original.y2, y2)
    }
  }

  describe("ScaledView.translateY") {
    val original = ScaledView(0 -> 10, 100 -> 200)
    it("should be able to be translate -10%") {
      val (x1, y1, x2, y2) = original.translateY(-0.1).toCoords
      Assert.assertEquals(original.x1, x1)
      Assert.assertEquals(90, y1)
      Assert.assertEquals(original.x2, x2)
      Assert.assertEquals(190, y2)
    }
    it("should be able to translate 10%") {
      val (x1, y1, x2, y2) = original.translateY(0.1).toCoords
      Assert.assertEquals(original.x1, x1)
      Assert.assertEquals(110, y1)
      Assert.assertEquals(original.x2, x2)
      Assert.assertEquals(210, y2)
    }
  }
}
