package edu.citadel.sel.ast


import edu.citadel.sel.Symbol
import edu.citadel.sel.Token


/**
 * The abstract syntax tree node for an adding expression.  An adding
 * expression is a binary expression where the operator is either "+" or "-".
 * A simple example would be "x + 5".
 *
 * @constructor Construct an adding expression with the operator ("+" or "-")
 *              and the two operands.
 */
class AddingExpr(val leftOperand  : Expression,
                 val operator     : Token,
                 val rightOperand : Expression) : Expression
  {
    override fun interpret(context : Context) : Double
        = if (operator.symbol === Symbol.plus)
              leftOperand.interpret(context) + rightOperand.interpret(context)
          else
              leftOperand.interpret(context) - rightOperand.interpret(context)


    init
      {
        assert(operator.symbol.isAddingOperator)
      }
  }
