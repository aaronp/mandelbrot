package com.porpoise.mandelbrot.io

object InputCommand extends Enumeration {
  type InputCommand = Value
  // direction arrow IDs occur after an 21/97 input on the keyboard and will clash with capital letters  
  val Up = Value(65)
  lazy val UpId = Up.id

  val Down = Value(66)
  lazy val DownId = Down.id

  val Right = Value(67)
  lazy val RightId = Right.id

  val Left = Value(68)
  lazy val LeftId = Left.id

  val Space = Value(32) // ' '
  lazy val SpaceId = Space.id

  val Plus = Value(43) // '+'
  lazy val PlusId = Plus.id

  val Minus = Value(95) // '-'
  lazy val MinueId = Minus.id

  val GetState = Value(99) // 'g'
  lazy val GetStateId = GetState.id

  val Reset = Value(114) // 'r'
  lazy val ResetId = Reset.id

  val Quit = Value(113) // 'q'
  lazy val QuitId = Quit.id

  val StartAutoPlay = Value(119) // 'w'
  lazy val StartAutoPlayId = StartAutoPlay.id

  val StopAutoPlay = Value(116) // 's'
  lazy val StopAutoPlayId = StopAutoPlay.id

}