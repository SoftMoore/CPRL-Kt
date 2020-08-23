package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a loop statement.
 */
class LoopStmt : Statement()
  {
    // labels used during code generation
    private val L1 = getNewLabel()   // label for start of loop
    private val L2 = getNewLabel()   // label for end of loop

    var whileExpr  : Expression? = null
    var statements : List<Statement> = emptyList()


    /**
     * Returns the label for the end of the loop statement.
     */
    fun getExitLabel() = L2


    override fun checkConstraints()
      {
// ...
      }


    override fun emit()
      {
// ...
      }
  }
