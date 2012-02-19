package com.porpoise.mandelbrot.model

import org.junit.Assert
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class ModelTest extends Spec with ShouldMatchers {

  describe("ScaledView.adjust") {
    val original = ScaledView(0 -> 10, 100 -> 200)
    it("should be able to be translated to the left on the x axis") {
      val (x1, y1, x2, y2) = original.adjust(-0.1, 1.0, 1.0).toCoords
      Assert.assertEquals(-1.0, x1)
      Assert.assertEquals(original.y1, y1)
      Assert.assertEquals(9.0, x2)
      Assert.assertEquals(original.y2, y2)
    }
    it("should be able to be translated to the right on the x axis") {
      val (x1, y1, x2, y2) = original.adjust(0.1, 1.0, 1.0).toCoords
      Assert.assertEquals(1.0, x1)
      Assert.assertEquals(original.y1, y1)
      Assert.assertEquals(11.0, x2)
      Assert.assertEquals(original.y2, y2)
    }
  }
}
