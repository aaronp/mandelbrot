package com.porpoise.mandelbrot.controller
import scala.actors.Actor
import com.porpoise.mandelbrot.actors.StoppableActor
import com.porpoise.mandelbrot.actors.TimedActor
import com.porpoise.mandelbrot.model.ComputeMandelbrotRequest
import com.porpoise.mandelbrot.model.GetStateRequest
import com.porpoise.mandelbrot.model.GetStateResponse
import com.porpoise.mandelbrot.model.MandelbrotResult
import com.porpoise.mandelbrot.model.ScaledView
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.SetStateRequest
import com.porpoise.mandelbrot.model.Size
import com.porpoise.mandelbrot.model.StartAutoPlay
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.model.StopAutoPlay
import com.porpoise.mandelbrot.model.TranslateXRequest
import com.porpoise.mandelbrot.model.TranslateYRequest
import com.porpoise.mandelbrot.model.UpdateRequest
import com.porpoise.mandelbrot.model.ZoomRequest
import com.porpoise.mandelbrot.Constants.N
import com.porpoise.mandelbrot.Constants.Percentage
import com.porpoise.mandelbrot.model.RenderRequest
import com.porpoise.mandelbrot.render.CharacterMap
import com.porpoise.mandelbrot.model.Result
import com.porpoise.mandelbrot.io.InputCommand

/**
 * Trait responsible for starting/stopping a timed actor
 */
trait TimedUpdate { thisActor: Actor =>
  private var timedCaller: Option[Actor] = None

  protected def newMessage: Any = UpdateRequest()

  protected def targetActor: Actor = thisActor

  private def onStart(delayInMillis: Int) = {

    def startTimer: Actor = TimedActor(targetActor, delayInMillis) { newMessage _ }

    timedCaller = timedCaller match {
      case None => Some(startTimer)
      case Some(t) => Some(t)
    }
  }

  protected def stopAutoPlay = {
    timedCaller foreach { t => t ! Stop() }
    timedCaller = None
  }

  def autoPlayHandler: PartialFunction[Any, Unit] = {

    case StartAutoPlay(delayInMillis) => onStart(delayInMillis)

    case stop: StopAutoPlay => stopAutoPlay
  }
}
