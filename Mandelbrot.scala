object Mandelbrot {
  type N = BigDecimal
  type Color = Int

  case class Result(pixelCoords : (Int, Int), scaledCoords : (N, N), result : Color)

  def calculate(x : N, y : N, depth : Int) = {
     7
  }

  def plot(fromXY : (Int, Int), toXY : (Int, Int), depth : Int) : Seq[Result] = {

val (x1,y1) = fromXY
	val (x2,y2) = toXY
	val scaleX = mapRange(x1)(x2)(-2.5)(1)_
	val scaleY = mapRange(y1)(y2)(-1)(1)_
	for {coordX <- x1 to x2
	     coordY <- y1 to y2
	     x = scaleX(coordX)
	     y = scaleY(coordY)
	     } yield {
    Result(coordX -> coordY, x -> y,calculate(x,y, depth))

 }
}

  def main(args : Array[String]) = {

	val fromXY = (5, 5)
	val toXY = (10,10)

        val depth = 100

	val results = plot(fromXY, toXY, depth)


    println(results.mkString("%n".format()))

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

