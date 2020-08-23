package edu.citadel.cprl


import edu.citadel.compiler.AbstractToken
import edu.citadel.compiler.Position


/**
 * This class instantiates the generic class AbstractToken for CPRL symbols.
 *
 * @constructor Construct a new token with the given symbol, position, and text.
 */
class Token(symbol : Symbol, position : Position, text : String)
    : AbstractToken<Symbol>(symbol, position, text)
  {
    /**
     * Construct a new token with symbol = Symbol.UNKNOWN.
     * Position and text are initialized to default values.
     */
    constructor() : this(Symbol.unknown, Position(), "")
  }
