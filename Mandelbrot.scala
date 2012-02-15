object Mandelbrot {
  type N = Double
  type Color = Int

  case class Result(pixelCoords : (Int, Int), scaledCoords : (N, N), result : Color)

  def calculateDepth(xOffset : N, yOffset : N, depth : Int) = {

     var x : N = 0
     var y : N = 0

     var iter = 0
     while(x*x + y*y < 4 && iter < depth) {
       val tempX = xOffset + (x*x - y*y)
       y = yOffset + (x*y*2)
       x = tempX
       iter = iter + 1
     }

     iter
  }

  def mapCoords(fromXY : (Int, Int), toXY : (Int, Int), depth : Int) : Seq[Result] = {

    val (x1,y1) = fromXY
    val (x2,y2) = toXY
    val scaleX = mapRange(x1)(x2)(-2.5)(1)_
    val scaleY = mapRange(y1)(y2)(-1)(1)_
    for {coordY <- y1 to y2
    	coordX <- x1 to x2
         x = scaleX(coordX)
         y = scaleY(coordY)
         } yield Result(coordX -> coordY, x -> y,calculateDepth(x,y, depth))

  }

def plot(results : Seq[Result]) = {
   var lastY = results.head.pixelCoords._2
   for (Result((x,y), _, color) <- results) {
     val character = color match {
       case 1 => '.'
       case 2 => '-'
       case 3 => 'x'
       case 4 => 'X'
       case x if (x < 20) => '*'
       case x if (x < 50) => '$'
       case _ => ' '
     }
     if (y == lastY)
       print(character)
     else {
       lastY = y
       println(character)
     }
   }
}

    def main(args : Array[String]) = {
      val fromXY = (0, 0)
      val toXY = (100,100)
      val depth = 1000
      val results = mapCoords(fromXY, toXY, depth)
      plot(results)
    }

    def mapRange(min : N)(max : N)(newMin : N)(newMax : N)(value : N) = {
        val diff = max - min
        val newDiff = newMax - newMin
        newMin + (value - min) / diff * newDiff
    }

}

