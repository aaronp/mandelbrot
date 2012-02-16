package com.porpoise.mandelbrot.io
import scala.actors.Actor

/** keep access private so it can only be interacted with via messages */
private class InputActor extends Actor {

  def act() = {
    react {
      //TODO
      case _ =>
        act()
    }
  }
}