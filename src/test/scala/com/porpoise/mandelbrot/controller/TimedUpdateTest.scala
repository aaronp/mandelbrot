package com.porpoise.mandelbrot.controller

import scala.actors.Actor
import org.junit.runner.RunWith
import org.junit.Assert
import org.scalatest.Spec
import com.porpoise.mandelbrot.model.StartAutoPlay
import com.porpoise.mandelbrot.model.StopAutoPlay
import org.scalatest.junit.JUnitRunner
import com.porpoise.mandelbrot.actors.StoppableActor
import com.porpoise.mandelbrot.model.Stop

@RunWith(classOf[JUnitRunner])
class TimedUpdateTest extends Spec {

  val testThread = Actor.self

  describe("TimedUpdate trait") {
    it("can be started and stopped") {

      /**
       * create a timer actor which can be started and stopped
       */
      object TimerActor extends Actor with TimedUpdate with StoppableActor {
        private var messages = (0 to 100).toList
        override def newMessage = messages match {
          case h :: tail => messages = tail; h
          case Nil => Assert.fail("reached the end of the messages")
        }
        override def targetActor = testThread

        def act() = react(autoPlayHandler orElse stopHandler)
      }
      TimerActor.start

      /**
       * start the timer actor
       */
      TimerActor ! StartAutoPlay(100)

      val firstMessage = testThread.receiveWithin(100) { case x => x }
      Assert.assertEquals(0, firstMessage)

      val secondMessage = testThread.receiveWithin(1000) { case x => x }
      Assert.assertEquals(1, secondMessage)

      TimerActor ! StopAutoPlay()
      TimerActor ! Stop()

    }
  }
}
