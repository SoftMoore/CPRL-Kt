package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Type


/**
 * The AST for an elsif part of an if statement.
 *
 * @property booleanExpr the boolean expression that, if true, will result
 *                       in the execution of the list of "then" statements.
 * @property thenStmts   the statements to be executed when the boolean
 *                       expression evaluates to true.
 * @property endIfLabel  the label at the end of the enclosing if statement
*/
class ElsifPart(private val booleanExpr : Expression, val thenStmts : List<Statement>)
    : AST()
  {
    lateinit var endIfLabel  : String
    private  val L1 : String = getNewLabel()    // label at end of thenStmts


    override fun checkConstraints()
      {
        try
          {
            booleanExpr.checkConstraints()

            for (stmt in thenStmts)
                stmt.checkConstraints()

            if (booleanExpr.type != Type.Boolean)
              {
                val errorMsg = "An \"elsif\" condition should have type Boolean."
                throw error(booleanExpr.position, errorMsg)
              }
          }
        catch (e : ConstraintException)
          {
            ErrorHandler.reportError(e)
          }
      }


    override fun emit()
      {
        booleanExpr.emitBranch(false, L1)

        // emit code for statements
        for (stmt in thenStmts)
            stmt.emit()

        // branch to end of if statement
        emit("BR $endIfLabel")

        // L1:
        emitLabel(L1)
      }
  }
