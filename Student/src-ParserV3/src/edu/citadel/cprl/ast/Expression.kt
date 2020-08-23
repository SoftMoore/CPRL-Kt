package edu.citadel.cprl.ast


import edu.citadel.compiler.CodeGenException
import edu.citadel.compiler.Position
import edu.citadel.cprl.Type


const val FALSE = "0"
const val TRUE = "1"


/**
 * Base class for all CPRL expressions.
 *
 * @constructor Construct an expression with the specified type and position.
 */
abstract class Expression(var type : Type, val position : Position) : AST()
  {
    /**
     * Construct an expression with the specified position.  Initializes
     * the type of the expression to UNKNOWN.
     */
    constructor(exprPosition : Position) : this(Type.UNKNOWN, exprPosition)

    /**
     * For Boolean expressions, the method emits the appropriate branch opcode
     * based on the condition.  For example, if the expression is a "&lt;"
     * relational expression and the condition is false, then the opcode "BGE"
     * is emitted.  The method defined in this class works correctly for Boolean
     * constants, Boolean named values, and "not" expressions.  It should be
     * overridden for other Boolean expression ASTs (e.g., RelationalExpr).
     *
     * @param condition  the condition that determines the branch to be emitted.
     * @param label      the label for the branch destination.
     *
     * @throws CodeGenException  if the method is unable to generate appropriate target code.
     */
    open fun emitBranch(condition : Boolean, label : String)
      {
        // default behavior unless overridden; correct for constants and named values
        assert(type == Type.Boolean) { "Expression type is not Boolean." }

        emit()  // leaves boolean value on top of stack
        emit(if (condition) "BNZ $label" else "BZ $label")
      }
  }
