package edu.citadel.cprl.ast


import edu.citadel.compiler.CodeGenException
import edu.citadel.cprl.Type


/**
 * Base class with common code for write and writeln statements.
 *
 * @constructor Construct an output statement with the list of expressions.
 */
abstract class OutputStmt(val expressions : List<Expression>) : Statement()
  {
    /**
     * Calls method checkConstraints() for each expression.
     */
    override fun checkConstraints()
      {
        for (expr in expressions)
            expr.checkConstraints()
      }


    /**
     * Emits code to write the value of each expression to standard output.
     */
    override fun emit()
      {
        for (expr in expressions)
          {
            expr.emit()

            when (expr.type)
              {
                Type.Integer -> emit("PUTINT")
                Type.Boolean -> emit("PUTBYTE")
                Type.Char    -> emit("PUTCH")
                Type.String  -> emit("PUTSTR")
                else         -> throw CodeGenException(expr.position, "Invalid type.")
              }
          }
      }
  }
