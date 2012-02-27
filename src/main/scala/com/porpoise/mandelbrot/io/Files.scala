package com.porpoise.mandelbrot.io
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

object Files {

  def writeToFile(file: File, content: String) = {
    val writer = new FileWriter(file)
    try { writer.write(content) } finally { writer.close }
  }

}