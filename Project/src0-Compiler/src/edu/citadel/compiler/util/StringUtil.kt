package edu.citadel.compiler.util


/**
 * Formats an integer as right-justified within the specified field
 * width by prepending a sufficient number of blank spaces.
 *
 * @return the formatted string representation for the integer.
 */
fun format(n : Int, fieldWidth : Int) : String
  {
    val intStr = n.toString()

    if (intStr.length >= fieldWidth)
        return intStr
    else
      {
        val buffer = StringBuffer(fieldWidth)

        for (i in intStr.length until fieldWidth)
            buffer.append(' ')

        buffer.append(intStr)

        return buffer.toString()
      }
  }
