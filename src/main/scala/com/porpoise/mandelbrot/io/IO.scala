package com.porpoise.mandelbrot
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class IO(inputStream: InputStream) {

  private val reader = new BufferedReader(new InputStreamReader(inputStream))

  def close = reader.close

  def readInt(label: String, defaultValue: Int): Option[Int] = {
    print("%s (default %s)".format(label, defaultValue))
    readLine(defaultValue) { _.toInt }
  }
  def readNext(): Int = inputStream.read()

  def readDouble(label: String, defaultValue: Double): Option[Double] = {
    print("%s (default %s)".format(label, defaultValue))
    readLine(defaultValue) { _.toDouble }
  }

  private def readLine[T](defaultValue: T)(f: String => T): Option[T] = {
    val charIn = reader.readLine
    try {
      Some(f(charIn))
    } catch {
      case e =>
        if (charIn.isEmpty) {
          Some(defaultValue)
        } else {
          println("Didn't understand '%s'".format(charIn))
          None
        }

    }
  }
}