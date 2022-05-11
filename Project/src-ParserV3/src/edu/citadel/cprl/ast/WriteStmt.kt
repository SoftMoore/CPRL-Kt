package edu.citadel.cprl.ast


/**
 * The abstract syntax tree node for a write statement.
 *
 * @constructor Construct a write statement with the list of expressions.
*/
class WriteStmt(expressions : List<Expression>) : OutputStmt(expressions)
  {
    override fun checkConstraints()
      {
        super.checkConstraints()

        // if checkConstraints() is called, then expressions should not be empty
        assert(expressions.isNotEmpty())
          { "A \"write\" statement must have an expression." }
      }

    // inherited emit() method is sufficient
  }
