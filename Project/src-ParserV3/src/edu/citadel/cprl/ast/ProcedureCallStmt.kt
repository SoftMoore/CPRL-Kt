package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Token


/**
 * The abstract syntax tree node for a procedure call statement.
 *
 * @constructor Construct a procedure call statement with its name token, the
 *              list of actual parameters being passed as part of the call,
 *              and a reference to the declaration of the procedure being called.
 */
class ProcedureCallStmt(private val procId   : Token,
                                actualParams : List<Expression>,
                        private val procDecl : ProcedureDecl)
    : Statement()
  {
    // We need a mutable list since, for var parameters, we
    // need to replace named values by variables
    val actualParams : MutableList<Expression> = actualParams.toMutableList()


    override fun checkConstraints()
      {
// ...
      }


    override fun emit()
      {
// ...
      }
  }
