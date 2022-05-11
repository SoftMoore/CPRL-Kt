package edu.citadel.cvm.assembler


import edu.citadel.compiler.AbstractToken
import edu.citadel.compiler.Position


/**
 * Instantiates the generic class AbstractToken for CVM assembly language symbols.
 */
class Token : AbstractToken<Symbol>
  {
    /**
     * Constructs a new Token with symbol = Symbol.unknown.
     * Position and text are initialized to null.
     */
    constructor() : super(Symbol.UNKNOWN, Position(), "")


    /**
     * Constructs a new token with the specified symbol, position, and text.
     */
    constructor(symbol : Symbol, position : Position, text : String = symbol.toString())
        : super(symbol, position, text)


    /**
     * Constructs a new Token with the specified symbol.  Position
     * and text are initialized to null  This constructor is
     * useful when replacing instructions during optimization.
     */
    constructor(symbol: Symbol) : super(symbol, Position(), "")


    /**
     * Constructs a new Token with the specified symbol and text.
     * Position is initialized to null.  This constructor is
     * useful when replacing instructions during optimization.
     */
    constructor(symbol: Symbol, text: String) : super(symbol, Position(), text)
  }
