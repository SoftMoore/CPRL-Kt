package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a read statement.
 *
 * @constructor Construct a read statement with the specified variable
 *              for storing the input.
 */
class ReadStmt(private val variable : Variable) : Statement()
  {
    override fun checkConstraints()
      {
        // input is limited to integers and characters
// ...
      }


    override fun emit()
      {
// ...
      }
  }
