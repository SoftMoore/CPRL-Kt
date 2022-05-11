package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Symbol
import edu.citadel.cprl.Token
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for an adding expression.  An adding expression
 * is a binary expression where the operator is an adding operator, "+" or "-".
 * A simple example would be "x + 5".
 *
 * @constructor Construct an adding expression with the operator ("+" or "-")
 *              and the two operands.
 */
class AddingExpr(leftOperand : Expression, operator : Token, rightOperand : Expression)
    : BinaryExpr(leftOperand, operator, rightOperand)
  {
    /**
     * Initialize the type of the expression to Integer.
     */
    init
      {
        assert(operator.symbol.isAddingOperator()) { "Operator is not an adding operator." }
        type = Type.Integer
      }


    override fun checkConstraints()
      {
        try
          {
            leftOperand.checkConstraints()
            rightOperand.checkConstraints()

            // can add/subtract only integers
            if (leftOperand.type != Type.Integer)
              {
                val errorMsg = "Left operand for expression should have type Integer."
                throw error(leftOperand.position, errorMsg)
              }

            if (rightOperand.type != Type.Integer)
              {
                val errorMsg = "Right operand for expression should have type Integer."
                throw error(rightOperand.position, errorMsg)
              }
          }
        catch (e : ConstraintException)
          {
            ErrorHandler.reportError(e)
          }
    }


    override fun emit()
      {
        leftOperand.emit()
        rightOperand.emit()

        if (operator.symbol == Symbol.plus)
            emit("ADD")
        else if (operator.symbol == Symbol.minus)
            emit("SUB")
      }
  }
