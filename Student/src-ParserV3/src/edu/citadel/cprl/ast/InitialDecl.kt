package edu.citadel.cprl.ast


import edu.citadel.cprl.ArrayType
import edu.citadel.cprl.Token
import edu.citadel.cprl.Type


/**
 * Base class for all initial declarations.
 *
 * @constructor Construct an initial declaration with its identifier and type.
 */
abstract class InitialDecl(identifier : Token, declType : Type)
    : Declaration(identifier, declType)
  {
    override fun checkConstraints()
      {
        assert(type == Type.Boolean || type == Type.Integer
            || type == Type.Char    || type is ArrayType)
            { "Invalid CPRL type." }
      }
  }
