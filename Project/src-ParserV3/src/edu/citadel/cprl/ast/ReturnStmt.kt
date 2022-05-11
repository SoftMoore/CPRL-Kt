package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.Position


/**
 * The abstract syntax tree node for a return statement.  A return
 * statement has a reference to the enclosing subprogram and an
 * expression for the value being returned, which may be null.
 *
 * @constructor Construct a return statement with a reference to the enclosing
 *              subprogram and the expression for the value being returned,
 *              which may be null.
 */
class ReturnStmt(private val subprogramDecl : SubprogramDecl,
                 private val returnExpr     : Expression?,
                 private val returnPosition : Position)
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
