package com.porpoise.mandelbrot.controller
import scala.actors.Actor
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest

class ControllerActor extends Actor with ControllerTrait {

  def act() = {
    loop {
      react(handle)
    }
  }
}