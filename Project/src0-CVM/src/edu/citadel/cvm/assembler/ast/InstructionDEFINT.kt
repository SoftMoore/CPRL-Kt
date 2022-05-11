package edu.citadel.cvm.assembler.ast


import edu.citadel.cvm.Constants
import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token


/**
 * This class implements the abstract syntax tree for the assembly
 * language pseudo instruction DEFINT.
 */
class InstructionDEFINT(labels : MutableList<Token>, opCode : Token, arg : Token)
    : InstructionOneArg(labels, opCode, arg)
  {
    /**
     * Must be overridden for pseudo opcodes since the opcode
     * doesn't actually occupy space in memory
     */
    override val size : Int
        get() = 0


    /**
     * Abstract method -- must be overridden even though
     * it plays no significant role for pseudo opcodes.
     */
    override val argSize : Int
        get() = Constants.BYTES_PER_INTEGER


    override fun assertOpCode()
      {
        assertOpCode(Symbol.DEFINT)
      }


    public override fun checkArgType()
      {
        defineIdAddress(arg, Constants.BYTES_PER_INTEGER)
        checkArgType(Symbol.identifier)
      }


    override fun emit()
      {
        // nothing to emit for pseudo opcode
      }
  }
