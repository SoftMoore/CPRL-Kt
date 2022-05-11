package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Symbol
import edu.citadel.cprl.Token
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a negation expression.  A negation
 * expression is a unary expression where the operator is either "-" or "+".
 * A simple example would be "-x".
 *
 * @constructor Construct a negation expression with the specified
 *              operator ("-") and operand.
 */
class NegationExpr(operator : Token, operand : Expression)
    : UnaryExpr(operator, operand)
  {
    /**
     * Initialize the type of the expression to Integer.
     */
    init
      {
        assert(operator.symbol == Symbol.minus) { "Operator is not \"-\" operator." }
        type =Type.Integer
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
