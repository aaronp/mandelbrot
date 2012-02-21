package com.porpoise.mandelbrot.actors
import java.lang.System.{currentTimeMillis => now}

import scala.actors.Actor
import scala.actors.DaemonActor

import com.porpoise.mandelbrot.actors.StoppableActor

private object Timeout
private class TimedActor(targetActor: Actor, intervalInMillis: Long, makeNewMessage: () => Any) extends DaemonActor with StoppableActor {

  def act() = {
    loopWhile(running) { receive(stopHandler orElse handler) }
  }

  private def handler: PartialFunction[Any, Unit] = {
    case Timeout => handleTimeout()
  }

  private def handleTimeout(): Unit = {
    val timestamp = now
    val msg = makeNewMessage()
    targetActor ! msg
    val sleepTime = intervalInMillis - (now - timestamp)
    if (sleepTime > 0) {
      Thread.sleep(sleepTime)
    }
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