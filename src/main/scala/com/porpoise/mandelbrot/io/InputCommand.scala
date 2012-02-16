package com.porpoise.mandelbrot.io

object InputCommand extends Enumeration {
  type InputCommand = Value
  val Up, Down, Left, Right, Space, Plus, Minus = Value
}