package edu.citadel.cprl.ast


/**
 * The abstract syntax tree node for a writeln statement.
 *
 * @constructor Construct a writeln statement with the list of expressions.
*/
class WritelnStmt(expressions : List<Expression>) : OutputStmt(expressions)
  {
    // inherited checkConstraints() method is sufficient


    override fun emit()
      {
// ...
      }
  }
