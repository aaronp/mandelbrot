package com.porpoise.mandelbrot.actors
import com.porpoise.mandelbrot.model.Stop
import scala.actors._
import System.{ currentTimeMillis => now }

private object Timeout
private class TimedActor(targetActor: Actor, intervalInMillis: Long, makeNewMessage: () => Any) extends Actor with StoppableActor {

  def act() = {
    loopWhile(running) { receive(stopHandler orElse handler) }
  }

  private def handler: PartialFunction[Any, Unit] = {
    case Timeout => handleTimeout()
  }

  private def handleTimeout(): Unit = {
    println("TimedActor.handleTimeout")
    val timestamp = now
    val msg = makeNewMessage()
    println("TimedActor.handleTimeout: sending " + msg)
    targetActor ! msg
    val sleepTime = intervalInMillis - (now - timestamp)
    println("sleeping for  " + sleepTime)
    if (sleepTime > 0) {
      Thread.sleep(sleepTime)
    }
    //    println("sending " + timedActor + " a timeout to " + timedActor + " and 'this' is " + this + " and actor self is " + Actor.self)
    Actor.self ! Timeout
  }
}

object TimedActor {
  def apply(target: Actor, intervalInMillis: Long)(messageProducer: () => Any): Actor = {
    val actor = new TimedActor(target, intervalInMillis, messageProducer)
    actor.start
    actor ! Timeout
    actor
  }
}