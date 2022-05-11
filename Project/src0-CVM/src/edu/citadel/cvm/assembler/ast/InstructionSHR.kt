package edu.citadel.cvm.assembler.ast


import edu.citadel.cvm.OpCode
import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token


/**
 * This class implements the abstract syntax tree for the assembly
 * language instruction SHR.
 */
class InstructionSHR(labels : MutableList<Token>, opCode : Token, arg : Token)
    : InstructionOneArg(labels, opCode, arg)
  {
    public override// 1 byte
    val argSize : Int
        get() = 1


    public override fun assertOpCode()
      {
        assertOpCode(Symbol.SHR)
      }


    public override fun checkArgType()
      {
        checkArgType(Symbol.intLiteral)

        // check that the value is in the range 0..31
        val argValue = argToInt()
        if (argValue < 0 || argValue > 31)
          {
            val errorPosition = arg.position
            val errorMessage = "Shift amount must be be in the range 0..31"

            throw error(errorPosition, errorMessage)
          }
      }


    override fun emit()
      {
        emit(OpCode.SHR)
        emit(argToByte())
      }
  }
