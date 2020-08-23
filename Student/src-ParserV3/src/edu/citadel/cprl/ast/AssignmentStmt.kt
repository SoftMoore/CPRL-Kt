package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.Position


/**
 * The abstract syntax tree node for an assignment statement.
 *
 * @property variable  the variable on the left side of the assignment operator
 * @property expr the expression on the right side of the assignment operator
 * @property assignPosition the position of the assignment operator (for error reporting)
 */
class AssignmentStmt(private val variable : Variable,
                     private val expr : Expression,
                     private val assignPosition : Position)
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
