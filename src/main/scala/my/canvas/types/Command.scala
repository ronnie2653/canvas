package my.canvas.types

/**
*  Created by Sandor Nagy on 1/23/16.
*/
sealed trait Command

case class Canvas(w: Int, h: Int) extends Command {
  require(w <= 80 && h <= 40, "Canvas cannot be larger than: width 80, height 40")
}

case class Line(x1: Int, y1: Int, x2: Int, y2: Int) extends Command {
  require(x1 == x2 || y1 == y2, "Requirements for Line: only horizontal or vertical lines supported!")
}

case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Command {
  require(x1 < x2 && y1 < y2, "Requirements for for Rectangle coordinates: x1 < x2 and y1 < y2!")
}

case class BucketFill(x: Int, y: Int, c: String) extends Command

case class Quit() extends Command {
  def exitFromProgram() = System.exit(0)
}

