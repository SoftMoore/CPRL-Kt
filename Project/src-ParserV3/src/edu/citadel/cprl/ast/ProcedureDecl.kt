package edu.citadel.cprl.ast


import edu.citadel.cprl.Token


/**
 * The abstract syntax tree node for a procedure declaration.
 *
 * @constructor Construct a procedure declaration with its name (an identifier).
 */
class ProcedureDecl(procId : Token) : SubprogramDecl(procId)
  {
    override fun emit()
      {
// ...
      }
  }
