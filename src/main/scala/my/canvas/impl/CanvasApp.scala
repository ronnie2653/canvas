package my.canvas.impl

/**
 * Created by Sandor Nagy on 1/23/16.
 */

import my.canvas.types._
import my.canvas.utils.CanvasUtils
import my.canvas.utils.UserInputs._

import scala.io.StdIn

/*
C w h Should create a new canvas of width w and height h.

L x1 y1 x2 y2 Should create a new line from (x1,y1) to (x2,y2). Currently only horizontal or
vertical lines are supported. Horizontal and vertical lines will be drawn using the 'x'
character.

R x1 y1 x2 y2 Should create a new rectangle, whose upper left corner is (x1,y1) and lower right
corner is (x2,y2). Horizontal and vertical lines will be drawn using the 'x' character.

B x y c Should fill the entire area connected to (x,y) with "colour" c. The behaviour of this is
the same as that of the "bucket fill" tool in paint programs.

Q Should quit the program.
*/

object CanvasApp extends App {

  var quitProgramme = false
  var maybeUserCanvas: Option[Array[Array[String]]] = None

  def parseUserInput(userInput: String): Command = {
    val input = userInput.split(" ")
    input(0) match {
      case C => Canvas(input(1), input(2))
      case L => Line(input(1), input(2), input(3), input(4))
      case R => Rectangle(input(1), input(2), input(3), input(4))
      case B => BucketFill(input(1), input(2), input(3))
      case Q => Quit()
    }
  }

  while (!quitProgramme) {
    print(s"enter code: ")
    val userInput = StdIn.readLine()
    try {
      val c: Command = parseUserInput(userInput)
      maybeUserCanvas = CanvasUtils.updateCanvas(c, maybeUserCanvas)
      CanvasUtils.printCanvas(maybeUserCanvas)
    } catch {
      case i: IllegalArgumentException => println(s"Error: ${i.getMessage}")
      case e: Exception => e.getStackTrace
    }
  }

}
