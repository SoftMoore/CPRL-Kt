package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for an exit statement.  An exit statement has
 * an optional "when" expression (which should be null if there is no "when"
 * expression) and a reference to the enclosing loop statement.
 *
 * @constructor Construct an exit statement with its optional "when"
 *              expression (which should be null if there is no "when"
 *              expression) and a reference to the enclosing loop statement.
 */
class ExitStmt(private val whenExpr : Expression?, private val loopStmt : LoopStmt)
    : Statement()
  {
    override fun checkConstraints()
      {
// ...
      }


    override fun emit()
      {
// ...
      }
  }
