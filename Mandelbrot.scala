object Mandelbrot {
  type N = BigDecimal

  case class Result(pixelCoords : (Int, Int), scaledCoords : (BigDecimal

  def main(args : Array[String]) = {

	val fromXY = (0, 0)
	val toXY = (20,40)

	val (x1,y1) = fromXY
	val (x2,y2) = toXY
	val scaleX = Scale.mapRange(x1)(x2)(-2.5)(1)_
	val scaleY = Scale.mapRange(y1)(y2)(-1)(1)_
	val colors = for {coordX <- x1 to x2
	     coordY <- y1 to y2
	     x = scaleX(coordX)
	     y = scaleY(coordY)
	     } yield calculate(x,y)


    }

    /**
     *
     */
    def mapRange(min : N)(max : N)(newMin : N)(newMax : N)(value : N) = {
	val diff = max - min
	val newDiff = newMax - newMin
	newMin + (value - min) / diff * newDiff
    }

}

