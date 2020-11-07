package edu.citadel.sel.ast


import edu.citadel.sel.Symbol
import edu.citadel.sel.Token


/**
 * The abstract syntax tree node for a multiplying expression.  A multiplying
 * expression is a binary expression where the operator is either "*" or "/".
 * A simple example would be "5*x".
 *
 * @constructor Construct a multiplying expression with the operator ("*" or "/")
 *              and the two operands.
 */
class MultiplyingExpr(val leftOperand  : Expression,
                      val operator     : Token,
                      val rightOperand : Expression) : Expression
  {
    override fun interpret(context : Context) : Double
        = if (operator.symbol == Symbol.times)
              leftOperand.interpret(context)*rightOperand.interpret(context)
          else
              leftOperand.interpret(context)/rightOperand.interpret(context)


    init
      {
        assert(operator.symbol.isMultiplyingOperator)
      }
  }
