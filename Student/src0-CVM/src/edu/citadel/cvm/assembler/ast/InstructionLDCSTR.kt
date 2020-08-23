package edu.citadel.cvm.assembler.ast


import edu.citadel.cvm.Constants
import edu.citadel.cvm.OpCode
import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token


/**
 * This class implements the abstract syntax tree for the assembly
 * language instruction LDCSTR.
 *
 * Note: Only one argument (the string literal) is specified for this instruction
 * in assembly language, but two args are generated for the CVM machine code.
 */
class InstructionLDCSTR(labels: MutableList<Token>, opCode: Token, arg : Token)
    : InstructionOneArg(labels, opCode, arg)
{
    // need to subtract 2 to handle the opening and closing quotes
    private val strLength : Int
        get() = arg.text.length - 2


    // Note:  We must return the size for both the integer arg and
    //        the string arg that will be generated in machine code
    public override val argSize : Int
        get() = Constants.BYTES_PER_INTEGER + Constants.BYTES_PER_CHAR*strLength


    public override fun assertOpCode()
      {
        assertOpCode(Symbol.LDCSTR)
      }


    public override fun checkArgType()
      {
        checkArgType(Symbol.stringLiteral)
      }


    override fun emit()
      {
        val strLength = strLength

        emit(OpCode.LDCSTR)
        emit(strLength)

        val text = arg.text

        // omit opening and closing quotes
        for (i in 1..strLength)
            emit(text[i])
      }
  }
