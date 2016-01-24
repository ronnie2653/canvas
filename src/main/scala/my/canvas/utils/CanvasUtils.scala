package my.canvas.utils

import my.canvas.types._

/**
 * Created by Sandor Nagy on 1/23/16.
 *
 * This class methods contains the logic that handles user commands.
 */
object CanvasUtils {

  val initalSymbol = " "
  val innerLineSymbol = "+"
  val horizontalSymbol = "-"
  val verticalSymbol = "|"

  /**
   * This method can handle different user commands:
   *  - Input is the user command and the matrix that represents the actual canvas.
   *  - Output is the updated matrix that represents the new canvas.
   * 
   * @param c the user command
   * @param maybeCanvas the matrix that represents the actual canvas.
   * @return returns the updated Canvas Matrix.
   */
  def updateCanvas(c: Command, maybeCanvas: Option[Array[Array[String]]]): Option[Array[Array[String]]] = {
    c match {
      case Canvas(w, h) => createCanvas(w, h)

      case Line(x1, y1, x2, y2) => addLine(maybeCanvas, x1, y1, x2, y2)

      case Rectangle(x1, y1, x2, y2) => addRectangle(maybeCanvas, x1, y1, x2, y2)

      case BucketFill(x, y, color) => doBucketFill(maybeCanvas, x, y, color)

      case q: Quit => executeQuit(q)
    }
  }

  /**
   * This method prints out the canvas to the standard output (console) to be visible for the user.
   *
   * @param maybeCanvas the matrix representation of the canvas to print out
   */
  def printCanvas(maybeCanvas: Option[Array[Array[String]]]) = {
    maybeCanvas.foreach(canvas =>
      canvas.foreach(row => {
        row.foreach(print)
        println()
      }
      )
    )
  }

  private def executeQuit(q: Quit): Option[Array[Array[String]]] = {
    q.exitFromProgram()
    None
  }

  private def doBucketFill(maybeCanvas: Option[Array[Array[String]]], x: Int, y: Int, color: String): Option[Array[Array[String]]] = {
    require(maybeCanvas.nonEmpty, "No canvas defined yet!")
    val (h, w) = maybeCanvas.map(canvas => (canvas.length, canvas(0).length)).get
    require(x < w && y < h, s"Bucket fill coordinate pointing outside of the canvas, give values lower or equal than $w and $h")
    maybeCanvas.map { canvas =>
      fillBucket(w, h)(canvas, x, y, color)
    }
  }

  private def addRectangle(maybeCanvas: Option[Array[Array[String]]], x1: Int, y1: Int, x2: Int, y2: Int): Option[Array[Array[String]]] = {
    require(maybeCanvas.nonEmpty, "No canvas defined yet!")
    val (h, w) = maybeCanvas.map(canvas => (canvas.length, canvas(0).length)).get
    require(x1 < w && x2 < w && y1 < h && y2 < h, s"Rectangle would be out of canvas, give values no hiegher $w and $h")
    maybeCanvas.map { canvas =>
      // horizontal line
      for (i <- x1 to x2) {
        canvas(y1)(i) = innerLineSymbol
        canvas(y2)(i) = innerLineSymbol
      }
      // vertical line
      for (j <- y1 to y2) {
        canvas(j)(x1) = innerLineSymbol
        canvas(j)(x2) = innerLineSymbol
      }
      canvas
    }
  }

  private[utils] def createCanvas(w: Int, h: Int): Option[Array[Array[String]]] = {
    // to get a w*h net area to drown on we need to increase width and height of the matrix
    val increasedWidth = w + 2
    val increasedHeight = h + 2
    val newCanvas = new Array[Array[String]](increasedHeight)
    for (i <- 0 until increasedHeight)
      newCanvas.update(i, Array.fill(increasedWidth)(initalSymbol)) // initialize Array with whitespace
    newCanvas.update(0, Array.fill(increasedWidth)(horizontalSymbol))
    newCanvas.update(increasedHeight - 1, Array.fill(increasedWidth)(horizontalSymbol))
    for (i <- 1 until increasedHeight - 1) {
      newCanvas(i).update(0, verticalSymbol)
      newCanvas(i).update(increasedWidth - 1, verticalSymbol)
    }
    Option(newCanvas)
  }

  private def addVerticalLine(i: Int): Array[String] = Array.fill(i)("+")

  // Using recursion to fill out the available spaces
  // No spaces left to fill out then we stop the recursion calls.
  private def fillBucket(w: Int, h: Int)(canvas: Array[Array[String]], x: Int, y: Int, c: String): Array[Array[String]] = {
    var canFill = true
    while (canFill) {
      if (canvas(x)(y) == initalSymbol) {
        canvas(x)(y) = c
        val (incX, decX, incY, decY) = (x + 1, x - 1, y + 1, y - 1)
        val (isIncX, isDecX, isIncY, isDecY) = (incX < w - 1, 0 < decX, incY < h - 1, 0 < decY)
        if (isIncX)
          fillBucket(w, h)(canvas, incX, y, c)
        if (isDecX)
          fillBucket(w, h)(canvas, decX, y, c)
        if (isIncY)
          fillBucket(w, h)(canvas, x, incY, c)
        if (isDecY)
          fillBucket(w, h)(canvas, x, decY, c)
        canFill = isIncX || isDecX || isIncY || isDecY // if no updates possible return false and break out from the loop
      } else {
        canFill = false
      }
    }
    canvas
  }

  private def addLine(maybeCanvas: Option[Array[Array[String]]], x1: Int, y1: Int, x2: Int, y2: Int): Option[Array[Array[String]]] = {
    require(maybeCanvas.nonEmpty, "No canvas defined yet!")
    val (h, w) = maybeCanvas.map(canvas => (canvas.length, canvas(0).length)).get
    require(x1 < w && x2 < w && y1 < h && y2 < h, s"Line would be out of canvas, give values no higher then $w and $h")
    maybeCanvas.map { canvas =>
      if (x1 == x2) {
        // vertical line
        for (j <- y1 to y2)
          canvas(j)(x1) = innerLineSymbol
      } else if (y1 == y2) {
        // horizontal line
        for (i <- x1 to x2)
          canvas(y1)(i) = innerLineSymbol
      } else {
        require(x1 == x2 || y1 == y2, "Please give values for vertical or horizontal line: x1==x2 or y1==y2")
      }
      canvas
    }
  }
}
