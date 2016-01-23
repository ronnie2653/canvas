package my.canvas.utils

import my.canvas.types._

/**
 * Created by Sandor Nagy on 1/23/16.
 */
object CanvasUtils {

  val initalSymbol = " "
  val innerLineSymbol = "+"
  val horizontalSymbol = "-"
  val verticalSymbol = "|"

  def parseInput(userInput: String): Request = {
    null
  }

  private def addVerticalLine(i: Int): Array[String] = Array.fill(i)("+")

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
        canFill = isIncX || isDecX || isIncY || isDecY // if no updates possible return false and break out the loop
      } else {
        canFill = false
      }
    }
    canvas
  }

  def updateCanvas(c: Request, maybeCanvas: Option[Array[Array[String]]]): Option[Array[Array[String]]] = {
    c match {
      case Canvas(h, w) =>
        // to get a h*w net area to drown on we need to increase width and height of the matrix
        val increasedWidth = w + 2
        val increasedHeight = h + 2
        val ll = new Array[Array[String]](increasedHeight)
        for (i <- 0 until increasedHeight)
          ll.update(i, Array.fill(increasedWidth)(initalSymbol)) // initialize Array with whitespace
        ll.update(0, Array.fill(increasedWidth)(horizontalSymbol))
        ll.update(increasedHeight - 1, Array.fill(increasedWidth)(horizontalSymbol))
        for (i <- 1 until increasedHeight - 1) {
          ll(i).update(0, verticalSymbol)
          ll(i).update(increasedWidth - 1, verticalSymbol)
        }
        Option(ll)
      case Line(x1, y1, x2, y2) =>
        require(maybeCanvas.nonEmpty, "No canvas defined yet!")
        val (h, w) = maybeCanvas.map(canvas => (canvas.length, canvas(0).length)).get
        // TODO: refactor these to method.
        require(x1 < w && x2 < w && y1 < h && y2 < h, s"Line would be out of canvas, give values no higher then $w and $h")
        maybeCanvas.foreach { canvas =>
          if (x1 == x2) {
            // vertical line
            for (j <- y1 to y2)
              canvas(j).update(x1, innerLineSymbol)
          } else if (y1 == y2) {
            // horizontal line
            for (i <- x1 to x2)
              canvas(y1)(i) = innerLineSymbol
          } else {
            require(x1 == x2 || y1 == y2, "Please give values for vertical or horizontal line: x1==x2 or y1==y2")
          }
        }
        maybeCanvas
      case Rectangle(x1, y1, x2, y2) =>
        require(maybeCanvas.nonEmpty, "No canvas defined yet!")
        val (h, w) = maybeCanvas.map(canvas => (canvas.length, canvas(0).length)).get
        require(x1 < w && x2 < w && y1 < h && y2 < h, s"Rectangle would be out of canvas, give values no hiegher $w and $h")
        maybeCanvas.foreach { canvas =>
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
        }
        maybeCanvas
      case BucketFill(x, y, color) =>
        require(maybeCanvas.nonEmpty, "No canvas defined yet!")
        val (h, w) = maybeCanvas.map(canvas => (canvas.length, canvas(0).length)).get
        require(x < w && y < h, s"Bucket fill coordinate pointing outside of the canvas, give values lower or equal than $w and $h")
        maybeCanvas.foreach { canvas =>
          fillBucket(w, h)(canvas, x, y, color)
        }
        maybeCanvas
      case q: Quit =>
        q.exitFromProgram()
        None
    }
  }

  def printCanvas(cm: Option[Array[Array[String]]]) = {
    cm.foreach(canvas =>
      canvas.foreach(row => {
        row.foreach(print)
        println()
      }
      )
    )
  }
}
