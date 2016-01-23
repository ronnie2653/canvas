package my.canvas

/**
*  Created by Sandor Nagy on 1/23/16.
*/


package object impl {
    implicit def fromString(s: String): Int = s.toInt
}
