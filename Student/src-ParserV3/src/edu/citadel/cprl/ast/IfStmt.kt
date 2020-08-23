package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for an if statement.
 *
 * @property booleanExpr the boolean expression that, if true, will result
 *                       in the execution of the list of "then" statements.
 * @property thenStmts   the statements to be executed when the boolean
 *                       expression evaluates to true.
 * @property elsifParts  the elsif clauses for the if statement.
 * @property elseStmts   the list of statements that are in the else clause.

 */
class IfStmt(private val booleanExpr : Expression,
                     val thenStmts   : List<Statement>,
                     val elsifParts  : List<ElsifPart>,
                     val elseStmts   : List<Statement>)
    : Statement()
  {
    // labels used during code generation
    private val L1 = getNewLabel()   // label of address at end of then statements
    private val L2 = getNewLabel()   // label of address at end of if statement


    override fun checkConstraints()
      {
        try
          {
            booleanExpr.checkConstraints()

            for (stmt in thenStmts)
                stmt.checkConstraints()

            for (part in elsifParts)
                part.checkConstraints()

            for (stmt in elseStmts)
                stmt.checkConstraints()

            if (booleanExpr.type != Type.Boolean)
              {
                val errorMsg = "An \"if\" condition should have type Boolean."
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
        // if expression evaluates to false, branch to L1
        booleanExpr.emitBranch(false, L1)

        // emit code for then statements
        for (stmt in thenStmts)
            stmt.emit()

        // if there are elsif parts or an else part, branch to end of if statement
        if (elsifParts.isNotEmpty() || elseStmts.isNotEmpty())
            emit("BR $L2")

        // L1:
        emitLabel(L1)

        // emit code for elsif statements
        for (part in elsifParts)
          {
            part.endIfLabel = L2
            part.emit()
          }

        // emit code for else statements
        for (stmt in elseStmts)
            stmt.emit()

        // L2:
        emitLabel(L2)
      }
  }
