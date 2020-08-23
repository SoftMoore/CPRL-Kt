package edu.citadel.cvm.assembler.ast


import edu.citadel.cvm.Constants
import edu.citadel.cvm.OpCode
import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token


/**
 * This class implements the abstract syntax tree for the assembly
 * language instruction LDCCH.
 */
class InstructionLDCCH(labels : MutableList<Token>, opCode : Token, arg : Token)
    : InstructionOneArg(labels, opCode, arg)
  {
    public override val argSize : Int
        get() = Constants.BYTES_PER_CHAR


    public override fun assertOpCode()
      {
        assertOpCode(Symbol.LDCCH)
      }


    public override fun checkArgType()
      {
        checkArgType(Symbol.charLiteral)
      }


    override fun emit()
      {
        val arg = arg.text[1]
        emit(OpCode.LDCCH)
        emit(arg)
      }
  }
