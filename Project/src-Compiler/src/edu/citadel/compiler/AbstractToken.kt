package edu.citadel.compiler


/**
 * This class encapsulates the properties of a language token.  A token
 * consists of a symbol (a.k.a., the token type), a position, and a string
 * that contains the text of the token.
 *
 * @constructor  Constructs a new Token with the given symbol, position, and text.
 */
abstract class AbstractToken<Symbol : Enum<Symbol>>
    (val symbol : Symbol, val position : Position, text : String)
  {
    var text : String = if (text.isEmpty()) symbol.toString() else text

    override fun toString() = text
  }
