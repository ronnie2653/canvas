package my.canvas

/**
 * Created by Sandor Nagy on 1/23/16.
 */
package object impl {
  /**
   * Implicit function for String to Int conversion.
   */
  implicit def fromStringToInt(s: String) = s.toInt

}

