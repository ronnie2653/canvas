package my.canvas.utils

/**
* Created by Sandor Nagy on 1/24/16.
*/

import my.canvas.types.Canvas
import org.scalatest._

class CanvasUtilsTest extends FlatSpec with Matchers {

  import CanvasUtilsTest.testCanvas

  "Create Canvas command" should "create a canvas with the specified width and height" in {
    val command = Canvas(4, 5)
    val initalCanvas = None
    val actual = CanvasUtils.createCanvas(4,5).get
    val expected = testCanvas
    actual should equal(expected)
  }

}

object CanvasUtilsTest {
  // width and height values for test
  private val testW = 4
  private val testH = 5
  val testCanvas = setUpTestCanvas(new Array[Array[String]](testH+2))
  // because of borders canvas needs +2 extra row

  private def setUpTestCanvas(testCanvas: Array[Array[String]]): Array[Array[String]] = {
    // first initialize with default value
    for (i <- 0 until testCanvas.length)
      testCanvas(i) = Array.fill(testW+2)(" ")

    // top border
    testCanvas(0)(0) = "-"
    testCanvas(0)(1) = "-"
    testCanvas(0)(2) = "-"
    testCanvas(0)(3) = "-"
    testCanvas(0)(4) = "-"
    testCanvas(0)(5) = "-"
    // bottom border
    testCanvas(6)(0) = "-"
    testCanvas(6)(1) = "-"
    testCanvas(6)(2) = "-"
    testCanvas(6)(3) = "-"
    testCanvas(6)(4) = "-"
    testCanvas(6)(5) = "-"
    // left side border
    testCanvas(1)(0) = "|"
    testCanvas(2)(0) = "|"
    testCanvas(3)(0) = "|"
    testCanvas(4)(0) = "|"
    testCanvas(5)(0) = "|"
    // right side border
    testCanvas(1)(5) = "|"
    testCanvas(2)(5) = "|"
    testCanvas(3)(5) = "|"
    testCanvas(4)(5) = "|"
    testCanvas(5)(5) = "|"

    testCanvas
  }

}
