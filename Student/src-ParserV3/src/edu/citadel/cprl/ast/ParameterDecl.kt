package edu.citadel.cprl.ast


import edu.citadel.cprl.ScopeLevel
import edu.citadel.cprl.Token
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a parameter declaration.
 *
 * @constructor Construct a parameter declaration with its identifier,
 *              type, and a boolean value that indicates if it is a
 *              variable parameter declaration.
 */
class ParameterDecl(paramId : Token, type : Type, val isVarParam : Boolean)
    : Declaration(paramId, type), NamedDecl
  {
    override var relAddr = 0      // relative address for this declaration

    override val scopeLevel = ScopeLevel.SUBPROGRAM   // always SUBPROGRAM for a parameter


    /**
     * The size (number of bytes) associated with this parameter declaration.
     * The size of a parameter declaration is the number of bytes associated
     * with its type, except for variable parameters, whose size is the number
     * of bytes of a memory address.
     */
    override val size : Int
        get() = if (isVarParam) Type.Address.size else type.size


    override fun checkConstraints()
      {
        assert(type != Type.UNKNOWN && type != Type.none)
            { "Invalid CPRL type in parameter declaration." }
      }
  }
