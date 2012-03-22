package com.porpoise.mandelbrot
import scala.annotation.implicitNotFound
import com.porpoise.mandelbrot.io.InputCommand.Down
import com.porpoise.mandelbrot.io.InputCommand.GetState
import com.porpoise.mandelbrot.io.InputCommand.Left
import com.porpoise.mandelbrot.io.InputCommand.Minus
import com.porpoise.mandelbrot.io.InputCommand.Plus
import com.porpoise.mandelbrot.io.InputCommand.Quit
import com.porpoise.mandelbrot.io.InputCommand.Reset
import com.porpoise.mandelbrot.io.InputCommand.Right
import com.porpoise.mandelbrot.io.InputCommand.Space
import com.porpoise.mandelbrot.io.InputCommand.StartAutoPlay
import com.porpoise.mandelbrot.io.InputCommand.StopAutoPlay
import com.porpoise.mandelbrot.io.InputCommand.Up
import com.porpoise.mandelbrot.io.InputReader
import com.porpoise.mandelbrot.model.GetStateRequest
import com.porpoise.mandelbrot.model.GetStateResponse
import com.porpoise.mandelbrot.model.MandelbrotRequest
import com.porpoise.mandelbrot.model.SetAbsoluteViewRequest
import com.porpoise.mandelbrot.model.SetStateRequest
import com.porpoise.mandelbrot.model.{ StartAutoPlay => Play }
import com.porpoise.mandelbrot.model.Stop
import com.porpoise.mandelbrot.model.{ StopAutoPlay => Pause }
import com.porpoise.mandelbrot.model.TranslateXRequest
import com.porpoise.mandelbrot.model.TranslateYRequest
import com.porpoise.mandelbrot.model.ZoomRequest
import com.porpoise.mandelbrot.io.InputCommand
import scala.actors.Actor
import com.porpoise.mandelbrot.model.ScaledView
import com.porpoise.mandelbrot.model.ScaledCoords
import com.porpoise.mandelbrot.io.Files

object Main {
  import InputCommand._

  def usage = """Usage:
    | Use the arrow keys to change the percentage the image is translated each frame.
    |
    | For example, if you enter left (<-), the x-axis translation will be set to -1%.
    | Every time the series is rendered, it will be translated to the left by 1%.
    |
    | If you enter <- again, then it will be changed to 2%.
    |
    | Each input will thus change the translation percentage by 1%.
    |
    | The space bar resets the view to the origin.
    |
    | The +/- keys will change the zoom by 1%, respectively.
    |
    | The 'q' key quits.
    |
    | The 'g' key requests the current state.
    |
    | The 'r' key resets the translation/scale percentages.
    |
    | The 'w' key starts auto-play, where the series will be re-rendered continuously 
    | using the current translation/scale percentages.
    |
    | The 's' key will pause auto-play.
    """.stripMargin

  def readLoop(config: Config)(onMessage: MandelbrotRequest => Boolean) = {
    import config._

    var reading = true
    while (reading) {
      val commandOpt = InputReader.readInput(in)

      val msgOpt: Option[MandelbrotRequest] = commandOpt.map(_ match {
        case Up => TranslateYRequest(-1 * adjustment)
        case Down => TranslateYRequest(adjustment)
        case Left => TranslateXRequest(-1 * adjustment)
        case Right => TranslateXRequest(adjustment)
        case Space => SetAbsoluteViewRequest(size = resolution)
        case Plus => ZoomRequest(-1 * adjustment)
        case Minus => ZoomRequest(adjustment)
        case Quit => Stop()
        case GetState => GetStateRequest()
        case Reset => SetStateRequest()
        case StartAutoPlay => Play(refreshRateInMillis)
        case StopAutoPlay => Pause()
      })

      reading = msgOpt match {
        case Some(msg) => onMessage(msg)
        case None => false
      }
    }
  }

  def main(args: Array[String]) = {

    def onShutdown(controller: Actor): ScaledView = {
      val resultFuture = controller !! GetStateRequest()
      val GetStateResponse(scaledView, resolution, _, _, _) = resultFuture()
      scaledView
    }

    println(usage)

    Config.withConfig(System.in) { config =>
      import config._

      // start by rendering the initial view
      controller ! SetAbsoluteViewRequest(size = resolution)

      readLoop(config) { msg =>

        val shouldContinue = msg != Stop()

        if (!shouldContinue) {
          val ScaledView(ScaledCoords(x1, y1), ScaledCoords(x2, y2)) = onShutdown(controller)

          Files.writeToFile(new java.io.File(".lastView"), "the last view was @ (%s, %s) X (%s, %s)".format(x1, y1, x2, y2))
        }

        controller ! msg

        shouldContinue
      }

    }

    println("Done")
  }

}