package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Symbol
import edu.citadel.cprl.Token
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a multiplying expression.  A multiplying
 * expression is a binary expression where the operator is a multiplying operator
 * such as "*", "/", or "mod".  A simple example would be "5*x".
 *
 * @constructor Construct a multiplying expression with the operator
 *              ("*", "/", or "mod") and the two operands.
 */
class MultiplyingExpr(leftOperand : Expression, operator : Token, rightOperand : Expression)
    : BinaryExpr(leftOperand, operator, rightOperand)
  {
    /**
     * Initialize the type of the expression to Integer.
     */
    init
      {
        assert(operator.symbol.isMultiplyingOperator())
            { "MultiplyingExpr: operator is not a multiplying operator." }
        type = Type.Integer
      }


    override fun checkConstraints()
      {
// ...
      }


    override fun emit()
      {
// ...
      }
  }
