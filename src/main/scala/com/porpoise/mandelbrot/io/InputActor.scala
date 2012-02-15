package com.porpoise.mandelbrot.io
import scala.actors.Actor

class InputActor extends Actor {

  def act() = {
    react {
      case _ =>
        act()
    }
  }
}