package edu.citadel.sel


import edu.citadel.compiler.Position
import edu.citadel.compiler.Source
import java.io.IOException


/**
 * A scanner for SEL with two-symbol lookahead.
 */
class Scanner(private val source : Source)
  {
    /**
     * The current symbol in the source file.
     */
    var symbol = Symbol.unknown
        private set

    /**
     * The position of the current symbol in the source file.
     */
    var position = Position()
        private set

    /**
     * The text of the current token in the source file;
     * e.g., for identifiers or literals.
     */
    private var text : String = ""

    /**
     * The current token in the source file.
     */
    val token : Token
        get() = Token(symbol, position, text)

    /**
     * The second symbol in the source file.
     */
    var peekSymbol = Symbol.unknown
        get()
          {
            advancePeek()
            return field
          }
        private set

    private var isPeekValid : Boolean = false


    /** Set values for peek properties. */
    private fun advancePeek()
      {
        if (!isPeekValid)
          {
            // save current values
            val savedSymbol   = symbol
            val savedPosition = position
            val savedText     = text

            advance()

            peekSymbol = symbol

            // restore saved current values
            symbol = savedSymbol
            position = savedPosition
            text = savedText

            isPeekValid = true
          }
      }

    private val scanBuffer : StringBuilder = StringBuilder(100)


    /**
     * Advance to the next symbol in the expression string.
     */
    @Throws(IOException::class, InterpreterException::class)
    fun advance()
      {
        if (isPeekValid)
          {
            // peekSymbol was previously used as the second lookahead
            // symbol and is now valid as the current symbol
            symbol      = peekSymbol
            isPeekValid = false
          }
        else
          {
            skipWhiteSpace()

            // currently at starting character of the next token
            position = source.charPosition
            text = ""

            if (source.currentChar == Source.EOF)
              {
                // set symbol but don't advance
                symbol = Symbol.EOF
              }
            else if (Character.isLetter(source.currentChar))
              {
                text   = scanIdentifier()
                symbol = Symbol.identifier
              }
            else if (Character.isDigit(source.currentChar))
              {
                text   = scanNumericLiteral()
                symbol = Symbol.numericLiteral
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
                            symbol = Symbol.EOL
                            source.advance()
                          }
                        else
                          {
                            error("Invalid end-of-line character.")
                          }
                      }
                    '\n' ->
                      {
                        symbol = Symbol.EOL
                        source.advance()
                      }
                    '+' ->
                      {
                        symbol = Symbol.plus
                        source.advance()
                      }
                    '-' ->
                      {
                        symbol = Symbol.minus
                        source.advance()
                      }
                    '*' ->
                      {
                        symbol = Symbol.times
                        source.advance()
                      }
                    '/' ->
                      {
                        symbol = Symbol.divide
                        source.advance()
                      }
                    '(' ->
                      {
                        symbol = Symbol.leftParen
                        source.advance()
                      }
                    ')' ->
                      {
                        symbol = Symbol.rightParen
                        source.advance()
                      }
                    '.' ->
                      {
                        symbol = Symbol.dot
                        source.advance()
                      }
                    '=' ->
                      {
                        symbol = Symbol.assign
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

    /**
     * Initialize scanner with its associated source and advance
     * to the first token.
     */
    init
      {
        advance() // advance to the first token
      }
  }
