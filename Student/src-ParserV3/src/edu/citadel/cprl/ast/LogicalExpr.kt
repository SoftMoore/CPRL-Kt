package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Symbol
import edu.citadel.cprl.Token
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a logical expression.  A logical expression
 * is a binary expression where the operator is either "and" or "or".  A simple
 * example would be "(x &gt; 5) and (y &lt; 0)".
 *
 * @constructor Construct a logical expression with the operator ("and" or "or")
 *              and the two operands.
 */
class LogicalExpr(leftOperand : Expression, operator : Token, rightOperand : Expression)
    : BinaryExpr(leftOperand, operator, rightOperand)
  {
    // labels used during code generation for short-circuit version
    private val L1 : String = getNewLabel()   // label at start of right operand
    private val L2 : String = getNewLabel()   // label at end of logical expression


    /**
     * Initialize the type of the expression to Boolean.
     */
    init
      {
        assert(operator.symbol.isLogicalOperator())
            { "LogicalExpression: operator is not a logical operator." }
        type = Type.Boolean
      }


    override fun checkConstraints()
      {
        try
          {
            leftOperand.checkConstraints()
            rightOperand.checkConstraints()

            if (leftOperand.type != Type.Boolean)
              {
                val errorMsg = "Left operand for a logical expression " +
                               "should have type Boolean."
                throw error(leftOperand.position, errorMsg)
              }

            if (rightOperand.type != Type.Boolean)
              {
                val errorMsg = "Right operand for a logical expression " +
                               "should have type Boolean."
                throw error(rightOperand.position, errorMsg)
              }
          }
        catch (e : ConstraintException)
          {
            ErrorHandler.reportError(e)
          }
      }


    /**
     * Uses short-circuit evaluation for logical expressions.
     */
    override fun emit()
      {
        // Note:  Unlike the various emitBranch methods, this method will leave the
        // value (true or false) of the logical expression on the top of the stack.

        // emit code to evaluate the left operand
        leftOperand.emit()

        if (operator.symbol == Symbol.andRW)
          {
            // if true, branch to code that will evaluate right operand
            emit("BNZ $L1")

            // otherwise, place "false" back on top of stack as value
            // for the compound "and" expression
            emit("LDCB $FALSE")
          }
        else   // operator.symbol must be Symbol.orRW
          {
            // if false, branch to code that will evaluate right operand
            emit("BZ $L1")

            // otherwise, place "true" back on top of stack as value
            // for the compound "or" expression
            emit("LDCB $TRUE")
          }

        // branch to code following the expression
        emit("BR $L2")

        // L1:
        emitLabel(L1)

        // evaluate the right operand and leave result on
        // top of stack as value for compound expression
        rightOperand.emit()

        // L2:
        emitLabel(L2)
      }
  }
