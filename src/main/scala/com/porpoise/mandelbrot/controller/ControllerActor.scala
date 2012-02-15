package com.porpoise.mandelbrot.controller
import scala.actors.Actor

class ControllerActor extends Actor {

  def act() = {
    react {
      case _ =>
        act()
    }
  }
}