import java.io._
object Mandelbrot {
  val NewLine = "%n".format()
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

  def mapCoords(fromXY : (Int, Int), toXY : (Int, Int), rangeX : (N, N), rangeY : (N, N), depth : Int) : Seq[Result] = {

    val (x1,y1) = fromXY
    val (x2,y2) = toXY
    val (xMin, xMax) = rangeX
    val (yMin, yMax) = rangeY
    val scaleX = mapRange(x1)(x2)(xMin)(xMax)_
    val scaleY = mapRange(y1)(y2)(yMin)(yMax)_
    for {coordY <- y1 to y2
    	coordX <- x1 to x2
         x = scaleX(coordX)
         y = scaleY(coordY)
         } yield Result(coordX -> coordY, x -> y,calculateDepth(x,y, depth))

  }

def formatResults(results : Seq[Result]) : String = {
   var lastY = results.head.pixelCoords._2
   val buffer = new StringBuilder
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
     buffer.append(character)
     if (y != lastY) {
       lastY = y
       buffer.append(NewLine)
     }
   }
   buffer.toString
}

class IO(inputStream : InputStream) {


   private val reader = new BufferedReader(new InputStreamReader(inputStream))

   def close = reader.close

   def readInt(label : String, defaultValue : Int) : Option[Int] = {
      print("%s (default %s)".format(label, defaultValue))
      read(defaultValue){ _.toInt }
   }
   def readDouble(label : String, defaultValue : Double) : Option[Double] = {
      print("%s (default %s)".format(label, defaultValue))
      read(defaultValue){ _.toDouble }
   }

   private def read[T]( defaultValue : T)(f : String=> T) : Option[T] = {
              val charIn = reader.readLine
	      try {
		Some(f(charIn))
	      } catch {
		case e => 
                   if(charIn.isEmpty) {
                      Some(defaultValue)
                   } else {
                      println("Didn't understand '%s'".format(charIn))
                      None
                   }

	      }
   }
}

def runNext(io : IO) : Option[String] = {
      val results = for{
          x1 <- io.readInt("From X: ", 0)
	  y1 <- io.readInt("From Y: ", 0)
          fromXY = (x1, y1)
	  x2 <- io.readInt("To X: ", 10)
	  y2 <- io.readInt("To Y: ", 10)
          toXY = (x2,y2)
	  scaleX1 <- io.readDouble("Scale X1 (between -2.5 and 1) : ", -2.5)
	  scaleX2 <- io.readDouble("Scale X2 (between %s and 1) : ".format(scaleX1), 1)
          scaleX = (scaleX1, scaleX2)
	  scaleY1 <- io.readDouble("Scale Y1 (between -1 and 1) : ", -1)
	  scaleY2 <- io.readDouble("Scale Y2 (between %s and 1) : ".format(scaleY1), 1)
          scaleY = (scaleY1, scaleY2)

} yield {
	      val depth = 1000
	      val results = mapCoords(fromXY, toXY, scaleX, scaleY, depth)
              formatResults(results)
          }
     results.headOption
}

    def main(args : Array[String]) = {

     val io = new IO(System.in)

     var resultOpt : Option[String] = runNext(io)
     while(resultOpt.isDefined) {
	println(resultOpt.get)
	println("enter 'quit' (or 'exit', or anything that's not a number) to quit")
        resultOpt = runNext(io)
     }
	println("Done")
         }

    def mapRange(min : N)(max : N)(newMin : N)(newMax : N)(value : N) = {
        val diff = max - min
        val newDiff = newMax - newMin
        newMin + (value - min) / diff * newDiff
    }

}

