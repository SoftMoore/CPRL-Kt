package edu.citadel.sel


import edu.citadel.compiler.Position
import edu.citadel.compiler.Source
import java.io.IOException


/**
 * A scanner for SEL with two-symbol lookahead.
 */
class Scanner(private val source : Source)
  {
    /** The current symbol in the source file. */
    var symbol = Symbol.unknown
        private set

    /** Position of the current symbol in the source file. */
    var position = Position()
        private set

    private var text : String = ""   // text of current token (for identifiers and literals)

    /** The current token in the source file. */
    val token : Token
        get() = Token(symbol, position, text)

    /** The lookahead symbol. */
    var peekSymbol = Symbol.unknown
        private set

    private var peekPosition = Position()   // position of peekSymbol
    private var peekText : String = ""      // text of peekSymbol

    private val scanBuffer= StringBuilder(100)


    /**
     * Initialize scanner by advancing through the first two tokens.
     */
    init
      {
        advance()       // load current and peek values
        advance()
      }


    /**
     * Advance to the next symbol in the expression string.
     */
    @Throws(IOException::class, InterpreterException::class)
    fun advance()
      {
        // assign peek values to current values
        symbol   = peekSymbol
        position = peekPosition
        text     = peekText

        skipWhiteSpace()

        // currently at starting character of the next token
        peekPosition = source.charPosition
        peekText = ""

        if (source.currentChar == Source.EOF)
          {
            // set peekSymbol but don't advance
            peekSymbol = Symbol.EOF
          }
        else if (Character.isLetter(source.currentChar))
          {
            peekText   = scanIdentifier()
            peekSymbol = Symbol.identifier
          }
        else if (Character.isDigit(source.currentChar))
          {
            peekText   = scanNumericLiteral()
            peekSymbol = Symbol.numericLiteral
          }
        else
          {
            when (source.currentChar.toChar())
              {
                '\r' ->
                  {
                    source.advance()
                    if (source.currentChar.toChar() == '\n')
                      {
                        peekSymbol = Symbol.EOL
                        source.advance()
                      }
                    else
                        error("Invalid end-of-line character.")
                  }
                '\n' ->
                   {
                    peekSymbol = Symbol.EOL
                    source.advance()
                   }
                '+' ->
                  {
                    peekSymbol = Symbol.plus
                    source.advance()
                  }
                '-' ->
                  {
                    peekSymbol = Symbol.minus
                    source.advance()
                  }
                '*' ->
                  {
                    peekSymbol = Symbol.times
                    source.advance()
                  }
                '/' ->
                  {
                    peekSymbol = Symbol.divide
                    source.advance()
                  }
                '(' ->
                  {
                    peekSymbol = Symbol.leftParen
                    source.advance()
                  }
                ')' ->
                  {
                    peekSymbol = Symbol.rightParen
                    source.advance()
                  }
                '.' ->
                  {
                    peekSymbol = Symbol.dot
                    source.advance()
                  }
                '=' ->
                  {
                    peekSymbol = Symbol.assign
                    source.advance()
                   }
                else ->
                  {
                    val errorMsg = "Invalid character \'" + source.currentChar + "\'"
                    error(errorMsg)
                  }
              }
          }
      }


    /**
     * Fast skip over tabs and spaces.
     */
    @Throws(IOException::class)
    private fun skipWhiteSpace()
      {
        while (Character.isWhitespace(source.currentChar))
            source.advance()
      }


    /**
     * Clear the scan buffer (makes it empty).
     */
    private fun clearScanBuffer()
      {
        scanBuffer.delete(0, scanBuffer.length)
      }


    /**
     * Scans characters in the source file for a valid identifier using the
     * lexical rule: identifier = letter ( letter | digit)* .
     *
     * @return the string of letters and digits for the identifier.
     */
    @Throws(IOException::class)
    private fun scanIdentifier() : String
      {
        assert(Character.isLetter(source.currentChar))
        clearScanBuffer()

        do
          {
            scanBuffer.append(source.currentChar.toChar())
            source.advance()
          }
        while (Character.isLetterOrDigit(source.currentChar))

        return scanBuffer.toString()
      }

    /**
     * Scans characters in the expression string for a valid numeric literal.
     * Assumes that getChar() is the first character of the numeric literal.
     *
     * @return the string of digits for the numeric literal.
     */
    @Throws(IOException::class, InterpreterException::class)
    private fun scanNumericLiteral() : String
      {
        assert(Character.isDigit(source.currentChar))
        clearScanBuffer()

        do
          {
            scanBuffer.append(source.currentChar.toChar())
            source.advance()
          }
        while (Character.isDigit(source.currentChar))

        if (source.currentChar.toChar() == '.')
          {
            scanBuffer.append(source.currentChar.toChar())
            source.advance()
            if (!Character.isDigit(source.currentChar))
                error("Invalid numeric literal")
            do
              {
                scanBuffer.append(source.currentChar.toChar())
                source.advance()
              }
            while (Character.isDigit(source.currentChar))
          }

        return scanBuffer.toString()
      }

    /**
     * Throws an exception with an appropriate error message.
     */
    @Throws(InterpreterException::class)
    private fun error(message : String)
      {
        val position : Position = source.charPosition
        val errorMsg = """*** Lexical error detected near position $position:\n"
|                    + "    $message"""
        throw InterpreterException(errorMsg)
      }
  }
