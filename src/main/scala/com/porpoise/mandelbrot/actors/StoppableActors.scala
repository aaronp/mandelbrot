package com.porpoise.mandelbrot.actors
import com.porpoise.mandelbrot.model.Stop
import scala.actors.Actor

trait StoppableActor { this: Actor =>
  protected var running = true

  // hook for subclasses
  protected def onStop() = {}

  // don't allow supclasses to override this, otherwise they might forget to call 'super'!
  private def stopInternal() = {
    println(getClass().getSimpleName() + " stopping")
    onStop()
    running = false
  }
  val stopHandler: PartialFunction[Any, Unit] = {
    case Stop() => stopInternal()
  }
}