package edu.citadel.cvm.assembler


import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.Position
import edu.citadel.compiler.ScannerException
import edu.citadel.compiler.Source

import kotlin.system.exitProcess


/**
 * Performs lexical analysis for CVM assembly language.
 *
 * @constructor Construct scanner with its associated source.
 */
class Scanner(private val source : Source)
  {
    /**  The current symbol in the source file.  */
    var symbol : Symbol = Symbol.UNKNOWN

    /**  The position of the current symbol in the source file.  */
    var position = Position()

    /**  The text of the current token in the source file; e.g., for identifiers or literals.  */
    private var text : String = ""

    /**  The current token in the source file.  */
    val token : Token
        get() = Token(symbol, position, text)

    private val scanBuffer : StringBuilder = StringBuilder(100)

    /** maps strings to opcode symbols  */
    private val opCodeMap: HashMap<String, Symbol> = HashMap(100)


    /**
     * Initialize scanner and advance to the first token.
     */
    init
      {
        // initialize HashMap with reserved word symbols
        val symbols = Symbol.values()
        for (symbol in symbols)
          {
            if (symbol.isOpCode)
                opCodeMap[symbol.toString()] = symbol
          }

        advance()           // advance to the first token
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
                // opcode symbol, identifier, or label
                val idString = scanIdentifier()
                val scannedSymbol = getIdentifierSymbol(idString)
                symbol = scannedSymbol

                if (scannedSymbol == Symbol.identifier)
                  {
                    // check to see if we have a label
                    if (source.currentChar.toChar() == ':')
                      {
                        symbol = Symbol.labelId
                        text   = "$idString:"
                        source.advance()
                      }
                    else
                        text = idString
                  }
              }
            else if (Character.isDigit(source.currentChar.toChar()))
              {
                symbol = Symbol.intLiteral
                text = scanIntegerLiteral()
              }
            else
                when (source.currentChar.toChar())
                  {
                    ';'  ->
                      {
                        skipComment()
                        advance()   // continue scanning for next token
                      }
                    '\'' ->
                      {
                        symbol = Symbol.charLiteral
                        text = scanCharLiteral()
                      }
                    '\"' ->
                      {
                        symbol = Symbol.stringLiteral
                        text = scanStringLiteral()
                      }
                    '-'  ->
                      {
                        // should be a negative integer literal
                        source.advance()
                        if (Character.isDigit(source.currentChar.toChar()))
                          {
                            symbol = Symbol.intLiteral
                            text = "-" + scanIntegerLiteral()
                          }
                        else
                          {
                            throw error("Expecting an integer literal")
                          }
                      }
                    else ->
                      {
                        // error:  invalid token
                        source.advance()
                        throw error("Invalid Token")
                      }
                  }
          }
        catch (e: ScannerException)
          {
            // stop on first error -- no error recovery
            ErrorHandler.reportError(e)
            exitProcess(1)
          }
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
        // assumes that source.getChar() is the first character of the identifier
        assert(Character.isLetter(source.currentChar.toChar()))
            { "Check identifier start for letter at position $position." }

        clearScanBuffer()

        do
          {
            scanBuffer.append(source.currentChar.toChar())
            source.advance()
          }
        while (Character.isLetterOrDigit(source.currentChar.toChar()))

        return scanBuffer.toString()
      }


    /**
     * Scans characters in the source file for a valid integer literal.
     * Assumes that source.getChar() is the first character of the Integer literal.
     *
     * @return the string of digits for the integer literal.
     */
    private fun scanIntegerLiteral() : String
      {
        // assumes that source.getChar() is the first digit of the integer literal
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


    private fun skipComment()
      {
        // assumes that source.getChar() is the leading ';'
        assert(source.currentChar.toChar() == ';')
            { "Check for ';' to start comment." }

        skipToEndOfLine()
        source.advance()
      }


    /**
     * Scans characters in the source file for a String literal.
     * Escaped characters are converted; e.g., '\t' is converted to
     * the tab character.  Assumes that source.getChar() is the
     * opening quote (") of the String literal.
     *
     * @return the string of characters for the string literal, including
     * opening and closing quotes
     */
    private fun scanStringLiteral() : String
      {
        // assumes that source.getChar() is the opening double quote for the string literal
        assert(source.currentChar.toChar() == '\"')
            { "Check for opening quote (\") at position $position." }

        clearScanBuffer()

        do
          {
            checkGraphicChar(source.currentChar)
            val c = source.currentChar.toChar()

            if (c == '\\')
                scanBuffer.append(scanEscapedChar())   // call to scanEscapedChar() advances source
            else
              {
                scanBuffer.append(c)
                source.advance()
              }
          }
        while (source.currentChar.toChar() != '\"')

        scanBuffer.append('\"')     // append closing quote
        source.advance()

        return scanBuffer.toString()
      }


    /**
     * Scans characters in the source file for a valid char literal.
     * Escaped characters are converted; e.g., '\t' is converted to the
     * tab character.  Assumes that source.getChar() is the opening
     * single quote (') of the Char literal.
     *
     * @return the string of characters for the char literal, including
     * opening and closing single quotes.
     */
    private fun scanCharLiteral() : String
      {
        // assumes that source.getChar() is the opening single quote for the char literal
        assert(source.currentChar.toChar() == '\'')
            { "Check for opening quote (\') at position $position." }

        clearScanBuffer()

        var c = source.currentChar.toChar()        // opening quote
        scanBuffer.append(c)                       // append the opening quote

        source.advance()
        checkGraphicChar(source.currentChar)
        c = source.currentChar.toChar()            // the character literal

        if (c == '\\')
          {
            // escaped character
            scanBuffer.append(scanEscapedChar())   // call to scanEscapedChar() advances source
          }
        else if (c == '\'')                        // check for empty char literal
          {
            source.advance()
            throw error("Char literal must contain exactly one character")
          }
        else
          {
            scanBuffer.append(c)                   // append the character literal
            source.advance()
          }

        checkGraphicChar(source.currentChar)
        c = source.currentChar.toChar()            // should be the closing quote

        if (c == '\'')                             // should be the closing quote
          {
            scanBuffer.append(c)                   // append the closing quote
            source.advance()
          }
        else
            throw error("Char literal not closed properly")

        return scanBuffer.toString()
      }


    /**
     * Scans characters in the source file for an escaped character; i.e.,
     * a character preceded by a backslash.  This method handles escape
     * characters \b, \t, \n, \f, \r, \", \', and \\.  If the character
     * following a backslash is anything other than one of these characters,
     * then an exception is thrown.  Assumes that source.getChar() is the
     * escape character (\).
     *
     * @return the value for an escaped character.
     */
    private fun scanEscapedChar() : Char
      {
        // assumes that source.getChar() is a backslash character
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
            'b'  -> return '\b'      // backspace
            't'  -> return '\t'      // tab
            'n'  -> return '\n'      // linefeed (a.k.a. newline)
            'f'  -> return '\u000C'  // form feed (can't use \f in  Kotlin)
            'r'  -> return '\r'      // carriage return
            '\"' -> return '\"'      // double quote
            '\'' -> return '\''      // single quote
            '\\' -> return '\\'      // backslash
            else -> throw ScannerException(backslashPosition, "Illegal escape character.")
          }
      }


    /**
     * Returns the symbol associated with an identifier
     * (Symbol.ADD, Symbol.AND, Symbol.identifier, etc.)
     */
    private fun getIdentifierSymbol(idString : String):  Symbol
      {
        val idLength = idString.length

        if (idLength < 2 || idLength > 7)     // quick check based on string length
            return Symbol.identifier

        val idSymbol = opCodeMap[idString.upperCase()]
        return idSymbol ?: Symbol.identifier
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
     * Checks that the integer represents a graphic character in the Unicode
     * Basic Multilingual Plane (BMP).
     *
     * @throws ScannerException if the integer does not represent a BMP graphic character.
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
            if (c == '\r' || c == '\n')
              {
                // special check for end of line
                throw error("Char and String literals can not extend past end of line.")
              }
            else if (Character.isISOControl(c))
              {
                // Sorry.  No ISO control characters.
                throw ScannerException(source.charPosition,
                        "Control characters not allowed in Char or String literal.")
              }
          }
      }


    /**
     * Throws a ScannerException with the specified error message.
     */
    private fun error(message: String) : ScannerException
      {
        return ScannerException(position, message)
      }


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
