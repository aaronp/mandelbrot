package com.porpoise.mandelbrot.actors
import com.porpoise.mandelbrot.model.Stop
import scala.actors._
import System.{ currentTimeMillis => now }

private object Timeout
private class TimedActor(targetActor: Actor, intervalInMillis: Long, makeNewMessage: () => Any) extends Actor with StoppableActor {

  def act() = {
    loopWhile(running) { receive(handler orElse stopHandler) }
  }

  private def handler: PartialFunction[Any, Unit] = {
    case Timeout => handleTimeout()
  }

  private def handleTimeout(): Unit = {
    val timestamp = now
    val msg = makeNewMessage
    targetActor ! msg
    val sleepTime = intervalInMillis - (now - timestamp)
    Thread.sleep(sleepTime)
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