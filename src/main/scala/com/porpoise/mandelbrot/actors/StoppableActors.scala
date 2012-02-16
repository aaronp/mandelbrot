package com.porpoise.mandelbrot.actors
import com.porpoise.mandelbrot.model.Stop

trait StoppableActor {
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
    //  }
    //  val notExpectedHandler: PartialFunction[Any, Unit] = {
    case other => println(getClass().getSimpleName() + " received unexpected message " + other)
  }
}