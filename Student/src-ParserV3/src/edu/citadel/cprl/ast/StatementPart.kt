package edu.citadel.cprl.ast


/**
 * The abstract syntax tree node for the statement part of a program.
 * A statement part contains a list of statements.
 *
 * @constructor Construct a statement part with the specified list of statements.
 */
class StatementPart(val statements : List<Statement> = emptyList()) : AST()
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
