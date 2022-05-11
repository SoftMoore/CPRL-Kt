package edu.citadel.cprl.ast


/**
 * The abstract syntax tree node for the declarative part of a program.
 *
 * @constructor Construct a declarative part with the lists of initial and
 *              subprogram declarations.
 */
class DeclarativePart(val initialDecls    : List<InitialDecl>    = emptyList(),
                      val subprogramDecls : List<SubprogramDecl> = emptyList())
    : AST()
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
