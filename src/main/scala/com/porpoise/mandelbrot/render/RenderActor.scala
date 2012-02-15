package com.porpoise.mandelbrot.render
import scala.actors.Actor

class RenderActor extends Actor {

  def act() = {
    react {
      case _ =>
        act()
    }
  }
}