package edu.citadel.sel.ast


import edu.citadel.sel.Symbol
import edu.citadel.sel.Token


/**
 * The abstract syntax tree node for a negation expression.  A negation
 * expression is a unary expression where the operator is "-".
 * A simple example would be "-x".
 *
 * @constructor Construct a negation expression with a negation operator token
 *              and an operand.
 */
class NegationExpr(operator : Token, val operand : Expression) : Expression
  {
    override fun interpret(context : Context) : Double = -operand.interpret(context)


    init
      {
        assert(operator.symbol == Symbol.minus)
      }
  }
