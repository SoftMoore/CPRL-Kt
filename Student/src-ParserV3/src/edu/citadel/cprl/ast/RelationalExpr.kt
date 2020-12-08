package edu.citadel.cprl.ast


import edu.citadel.compiler.CodeGenException
import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Symbol
import edu.citadel.cprl.Token
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a relational expression.  A relational
 * expression is a binary expression where the operator is a relational
 * operator such as "<=" or ">".  A simple example would be "x < 5".
 *
 * @constructor Construct a relational expression with the operator
 *              ("=", "<=", etc.) and the two operands.
 */
class RelationalExpr(leftOperand : Expression, operator : Token, rightOperand : Expression)
    : BinaryExpr(leftOperand, operator, rightOperand)
  {
    // labels used during code generation
    private val L1 : String = getNewLabel()   // label at start of right operand
    private val L2 : String = getNewLabel()   // label at end of the relational expression


    /**
     * Initialize the type of the expression to Boolean.
     */
    init
      {
        assert(operator.symbol.isRelationalOperator())
            { "Operator is not a relational operator." }
        type = Type.Boolean
      }


    override fun checkConstraints()
      {
// ...
      }


    override fun emit()
      {
        emitBranch(false, L1)

        // emit true
        emit("LDCB $TRUE")

        // jump over code to emit false
        emit("BR $L2")

        // L1:
        emitLabel(L1)

        // emit false
        emit("LDCB $FALSE")

        // L2:
        emitLabel(L2)
      }


    override fun emitBranch(condition : Boolean, label : String)
      {
        emitOperands()
        emit("CMP")

        when (operator.symbol)
          {
            Symbol.equals         -> emit(if (condition) "BZ $label" else "BNZ $label")
            Symbol.notEqual       -> emit(if (condition) "BNZ $label" else "BZ $label")
            Symbol.lessThan       -> emit(if (condition) "BL $label" else "BGE $label")
            Symbol.lessOrEqual    -> emit(if (condition) "BLE $label" else "BG $label")
            Symbol.greaterThan    -> emit(if (condition) "BG $label" else "BLE $label")
            Symbol.greaterOrEqual -> emit(if (condition) "BGE $label" else "BL $label")
            else                  -> throw CodeGenException(operator.position,
                                                            "Invalid relational operator.")
          }
      }


    private fun emitOperands()
      {
        // Relational operators compare integers only, so we need to make sure
        // that we have enough bytes on the stack.  Pad with zero bytes.
        for (n in 1..Type.Integer.size - leftOperand.type.size)
            emit("LDCB 0")

        leftOperand.emit()

        for (n in 1..Type.Integer.size - rightOperand.type.size)
            emit("LDCB 0")

        rightOperand.emit()
      }
  }
