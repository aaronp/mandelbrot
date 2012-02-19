package com.porpoise.mandelbrot.model

import org.junit.Assert
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class ModelTest extends BaseSpec {

  val original = ScaledView(0 -> 10, 100 -> 200)

  describe("ScaledView.zoom") {
    it("should be able to zoom 20%") {
      val (x1, y1, x2, y2) = original.zoom(0.2).toCoords
      println("zoomed:" + original.zoom(0.2))
      assertEquals(4, x1)
      assertEquals(6, x2)
      assertEquals(140, y1)
      assertEquals(160, y2)
    }
    it("should be able to zoom -20%") {
      val (x1, y1, x2, y2) = original.zoom(-0.2).toCoords
      assertEquals(-1.0, x1)
      assertEquals(11, x2)
      assertEquals(90, y1)
      assertEquals(210, y2)
    }
  }

  describe("ScaledView.translateX") {
    it("should be able to be translate -10%") {
      val (x1, y1, x2, y2) = original.translateX(-0.1).toCoords
      assertEquals(-1.0, x1)
      assertEquals(original.y1, y1)
      assertEquals(9.0, x2)
      assertEquals(original.y2, y2)
    }
    it("should be able to translate 10%") {
      val (x1, y1, x2, y2) = original.translateX(0.1).toCoords
      assertEquals(1.0, x1)
      assertEquals(original.y1, y1)
      assertEquals(11.0, x2)
      assertEquals(original.y2, y2)
    }
  }

  describe("ScaledView.translateY") {
    it("should be able to be translate -10%") {
      val (x1, y1, x2, y2) = original.translateY(-0.1).toCoords
      assertEquals(original.x1, x1)
      assertEquals(90, y1)
      assertEquals(original.x2, x2)
      assertEquals(190, y2)
    }
    it("should be able to translate 10%") {
      val (x1, y1, x2, y2) = original.translateY(0.1).toCoords
      assertEquals(original.x1, x1)
      assertEquals(110, y1)
      assertEquals(original.x2, x2)
      assertEquals(210, y2)
    }
  }
}
