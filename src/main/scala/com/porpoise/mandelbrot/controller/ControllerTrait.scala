package com.porpoise.mandelbrot.controller

import com.porpoise.mandelbrot.model.MandelbrotRequest
import com.porpoise.mandelbrot.model.MandelbrotResponse
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.SetAbsoluteViewResponse

trait ControllerTrait {
  def controllerHandlers: PartialFunction[Any, Unit] = ControllerActions.actions
}

object ControllerActions {
  val actions: PartialFunction[Any, Unit] = {
    case r: SetAbsoluteViewRequest => SetAbsoluteViewResponse(r, Nil)
  }
}