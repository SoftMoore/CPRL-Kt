package edu.citadel.cprl


import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.Position
import edu.citadel.compiler.ScannerException
import edu.citadel.compiler.Source


/**
 * Performs lexical analysis for the CPRL programming language.
 *
 * @constructor Construct scanner with its associated source.
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
    private var text = ""

    /**
     * The current token in the source file.
     */
    val token : Token
        get() = Token(symbol, position, text)

    private val scanBuffer = StringBuilder(100)


    /**
     * Initialize scanner and advance to the first token.
     */
    init
      {
        advance()   // advance to the first token
      }


    /**
     * Advance to the next token in the source file.
     */
    fun advance()
      {
        try
          {
            skipWhiteSpace()

            // currently at starting character of next token
            position = source.charPosition
            text = ""

            if (source.currentChar == Source.EOF)
              {
                // set symbol but don't advance
                symbol = Symbol.EOF
              }
            else if (Character.isLetter(source.currentChar.toChar()))
              {
                val idString = scanIdentifier()
                symbol = getIdentifierSymbol(idString)

                if (symbol == Symbol.identifier)
                    text = idString
              }
            else if (Character.isDigit(source.currentChar.toChar()))
              {
                symbol = Symbol.intLiteral
                text   = scanIntegerLiteral()
              }
            else
              {
                when (source.currentChar.toChar())
                  {
                    '+'  ->
                      {
                        symbol = Symbol.plus
                        source.advance()
                      }

// ...   Hint for comments: Recursion

                    '>'  ->
                      {
                        source.advance()
                        if (source.currentChar.toChar() == '=')
                          {
                            symbol = Symbol.greaterOrEqual
                            source.advance()
                          }
                        else
                            symbol = Symbol.greaterThan
                      }

// ...

                    else           // error:  invalid character
                         ->
                      {
                        val errorMsg = ("Invalid character \'${source.currentChar.toChar()}\'")
                        source.advance()
                        throw error(errorMsg)
                      }
                  }
              }
          }
        catch (e : ScannerException)
          {
            ErrorHandler.reportError(e)

            // set token to either EOF or UNKNOWN
            symbol = if (source.currentChar == Source.EOF) Symbol.EOF else Symbol.unknown
          }
      }


    /**
     * Returns the symbol associated with an identifier
     * (Symbol.arrayRW, Symbol.ifRW, Symbol.identifier, etc.)
     */
    private fun getIdentifierSymbol(idString : String) : Symbol
      {
// ...  Hint: Need an efficient search based on the text of the identifier (parameter idString)
      }


    /**
     * Skip over a comment.
     */
    private fun skipComment()
      {
// ...
      }


    /**
     * Advance until the symbol in the source file matches the symbol
     * specified in the parameter or until end of file is encountered.
     */
    fun advanceTo(symbol : Symbol)
      {
        while (true)
          {
            if (this.symbol == symbol || source.currentChar == Source.EOF)
                return
            else
                advance()
          }
      }


    /**
     * Advance until the symbol in the source file matches one of the
     * symbols in the given array or until end of file is encountered.
     */
    fun advanceTo(symbols : Array<Symbol>)
      {
        while (true)
          {
            if (search(symbols, symbol) >= 0 || source.currentChar == Source.EOF)
                return
            else
                advance()
          }
      }


    /**
     * Performs a linear search of the array for the given value.
     *
     * @return the index of the value in the array if found, otherwise -1.
     */
    private fun search(symbols : Array<Symbol>, value : Symbol) : Int
      {
        for (i in symbols.indices)
          {
            if (symbols[i] == value)
                return i
          }

        return -1
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
    private fun scanIdentifier() : String
      {
// ...
      }


    /**
     * Scans characters in the source file for a valid integer literal.
     * Assumes that source.currentChar is the first character of the Integer literal.
     *
     * @return the string of digits for the integer literal.
     */
    private fun scanIntegerLiteral() : String
      {
        // assumes that source.currentChar is the first digit of the integer literal
        assert(Character.isDigit(source.currentChar.toChar()))
            { "Check integer literal start for digit at position $position." }

        clearScanBuffer()

        do
          {
            scanBuffer.append(source.currentChar.toChar())
            source.advance()
          }
        while (Character.isDigit(source.currentChar.toChar()))

        return scanBuffer.toString()
      }


    /**
     * Scan characters in the source file for a String literal.  Escaped
     * characters are not converted; e.g., '\t' is not converted to the tab
     * character since the assembler performs the conversion.  Assumes that
     * source.currentChar is the opening double quote (") of the String literal.
     *
     * @return the string of characters for the string literal, including
     *         opening and closing quotes
     */
    private fun scanStringLiteral() : String
      {
// ...
      }


    /**
     * Scan characters in the source file for a Char literal.  Escaped
     * characters are not converted; e.g., '\t' is not converted to the tab
     * character since the assembler performs that conversion.  Assumes that
     * source.currentChar is the opening single quote (') of the Char literal.
     *
     * @return the string of characters for the char literal, including
     *         opening and closing single quotes.
     */
    private fun scanCharLiteral() : String
      {
        // assumes that source.currentChar is the opening single quote for the char literal
        assert(source.currentChar.toChar() == '\'')
          { "Check for opening quote (\') at position $position." }

        val errorMsg = "Invalid Char literal."
        clearScanBuffer()

        // append the opening single quote
        var c = source.currentChar.toChar()
        scanBuffer.append(c)
        source.advance()

        checkGraphicChar(source.currentChar)
        c = source.currentChar.toChar()

        if (c == '\\')   // escaped character
          {
            try
              {
                scanBuffer.append(scanEscapedChar())
              }
            catch (e : ScannerException)
              {
                checkGraphicChar(source.currentChar)
                c = source.currentChar.toChar()

                if (c == '\'')   // assume bad escape similar to '\x'
                  {
                    source.advance()     // move past closing single quote
                    throw e              // rethrow the exception
                  }
              }

          }
        else if (c == '\'')   // either '' (empty) or '''; both are invalid
          {
            source.advance()
            c = source.currentChar.toChar()

            if (c == '\'')   // three single quotes in a row
                source.advance()

            throw error(errorMsg)
          }
        else
          {
            scanBuffer.append(c)
            source.advance()
          }

        c = source.currentChar.toChar()   // should be the closing single quote
        checkGraphicChar(c.toInt())

        if (c == '\'')   // should be the closing single quote
        {
            scanBuffer.append(c)          // append the closing quote
            source.advance()
        }
        else
            throw error(errorMsg)

        return scanBuffer.toString()
      }


    /**
     * Scans characters in the source file for an escaped character; i.e.,
     * a character preceded by a backslash.  This method checks escape
     * characters \b, \t, \n, \f, \r, \", \', and \\.  If the character
     * following a backslash is anything other than one of these characters,
     * then an exception is thrown.  Note that the escaped character sequence
     * is returned unmodified; i.e., \t returns "\t", not the tab character.
     * Assumes that source.currentChar is the escape character (\).
     *
     * @return the escaped character sequence unmodified.
     */
    private fun scanEscapedChar() : String
      {
        // assumes that source.currentChar is the backslash for the escaped char
        assert(source.currentChar.toChar() == '\\')
            { "Check for escape character ('\\') at position $position." }

        // Need to save current position for error reporting.
        val backslashPosition = source.charPosition

        source.advance()
        checkGraphicChar(source.currentChar)
        val c = source.currentChar.toChar()

        source.advance()  // leave source at second character following the backslash

        when (c)
          {
            'b'  -> return "\\b"    // backspace
            't'  -> return "\\t"    // tab
            'n'  -> return "\\n"    // linefeed (a.k.a. newline)
            'f'  -> return "\\f"    // form feed
            'r'  -> return "\\r"    // carriage return
            '\"' -> return "\\\""   // double quote
            '\'' -> return "\\\'"   // single quote
            '\\' -> return "\\\\"   // backslash
            else -> {
                      // report error but return the invalid string
                      val errMessage = "Illegal escape character."
                      val ex = ScannerException(backslashPosition, errMessage)
                      ErrorHandler.reportError(ex)
                      return "\\$c"
                    }
          }
      }


    /**
     * Fast skip over white space.
     */
    private fun skipWhiteSpace()
      {
        while (Character.isWhitespace(source.currentChar.toChar()))
            source.advance()
      }


    /**
     * Advances over source characters to the end of the current line.
     */
    private fun skipToEndOfLine()
      {
        while (source.currentChar.toChar() != '\n')
          {
            source.advance()
            checkEOF()
          }
      }


    /**
     * Checks that the integer represents a graphic character in the Unicode
     * Basic Multilingual Plane (BMP).
     *
     * @throws ScannerException if the integer does not represent a BMP graphic
     * character.
     */
    private fun checkGraphicChar(n : Int)
      {
        if (n == Source.EOF)
            throw error("End of file reached before closing quote for Char or String literal.")
        else if (n > 0xffff)
            throw error("Character not in Unicode Basic Multilingual Pane (BMP)")
        else
          {
            val c = n.toChar()
            if (c == '\r' || c == '\n')           // special check for end of line
                throw error("Char and String literals can not extend past end of line.")
            else if (Character.isISOControl(c))   // Sorry.  No ISO control characters.
                throw ScannerException(source.charPosition,
                        "Control characters not allowed in Char or String literal.")
          }
      }


    /**
     * Returns a ScannerException with the specified error message.
     */
    private fun error(message : String) = ScannerException(position, message)


    /**
     * Used to check for EOF in the middle of scanning tokens that
     * require closing characters such as strings and comments.
     *
     * @throws ScannerException if source is at end of file.
     */
    private fun checkEOF()
      {
        if (source.currentChar == Source.EOF)
            throw error("Unexpected end of file")
      }
  }
