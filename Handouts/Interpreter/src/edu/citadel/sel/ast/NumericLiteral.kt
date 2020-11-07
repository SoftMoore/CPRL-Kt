package edu.citadel.sel.ast


import edu.citadel.sel.Symbol
import edu.citadel.sel.Token


/**
 * The abstract syntax tree node for a numeric literal expression.  A numeric
 * expression is simply a numeric literal.  A simple example would be "5".
 *
 * @constructor Construct a numeric literal expression with a numeric literal token.
 */
class NumericLiteral(literalToken : Token) : Expression
  {
    private val literalValue = literalToken.text.toDouble()

    override fun interpret(context : Context) : Double
      {
        // note that context is ignored for a numeric literal
        return literalValue
      }

    init
      {
        assert(literalToken.symbol == Symbol.numericLiteral)
      }
  }
