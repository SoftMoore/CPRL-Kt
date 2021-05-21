package edu.citadel.compiler


import java.io.*


/**
 * This class encapsulates the source file reader.  It maintains
 * the position of each character in the source file.
 *
 * @constructor Initialize Source with a Reader.
 */
class Source(private val sourceReader : Reader)
  {
    /**
     * The current character (as an Int) in the source file.  Property
     * has the value EOF (-1) if the end of file has been reached.
     */
    var currentChar = 0
        private set

    /**
     * The source line number of the current character.
     */
    private var lineNumber = 1


    /**
     * The offset of the current character within its line.
     */
    private var charNumber = 0


    /**
     * The position (line number, char number) of the current character
     * in the source file.
     */
    val charPosition : Position
        get() = Position(lineNumber, charNumber)


    /**
     * Advance to the first character.
     */
    init
      {
        advance()   // advance to the first character
      }


    /**
     * Advance to the next character in the source file.
     */
    fun advance()
      {
        if (currentChar == '\n'.code)
          {
            ++lineNumber
            charNumber = 1
          }
        else
            ++charNumber

        currentChar = sourceReader.read()
      }


    companion object
      {
        /** A constant representing end of file. */
        const val EOF = -1
      }
  }
