package edu.citadel.cprl.ast


import edu.citadel.cprl.ScopeLevel
import edu.citadel.cprl.Token
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a variable declaration.  Note that a variable
 * declaration is simply a container for a list of single variable declarations
 * (SingleVarDecls).
 *
 * @constructor Construct a variable declaration with its list of identifier tokens,
 *              type, and scope level
 */
class VarDecl(identifiers : List<Token>, varType : Type, scopeLevel : ScopeLevel)
    : InitialDecl(Token(), varType)
  {
    // the list of single variable declarations for the variable declaration
    val singleVarDecls = ArrayList<SingleVarDecl>(identifiers.size)

    init
      {
        for (id in identifiers)
            singleVarDecls.add(SingleVarDecl(id, varType, scopeLevel))
      }
  }
