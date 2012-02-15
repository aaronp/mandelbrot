package com.porpoise.mandelbrot.controller
import scala.actors.Actor
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.actors.StoppableActor

class ControllerActor extends Actor
  with ControllerTrait with StoppableActor {

  def act() = {
    loopWhile(running) {
      react(controllerHandlers orElse stopHandler)
    }
  }
}