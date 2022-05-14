package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token
import edu.citadel.cvm.assembler.ast.*


/**
 * Replaces LDCB 0 with LDCB0, LDCB 1 with LDCB1, LDCINT 0 with LDCINT0, and
 * LDCINT 1 with LDCINT1.  Implementation Note: This optimization must be
 * applied after the other optimizations or else they will need to be rewritten.
 * For example, the inc/dec instructions look for LDCINT 1 and not LDCINT1.
 */
class LoadSpecialConstants : Optimization
  {
    override fun optimize(instructions : MutableList<Instruction>, instNum : Int)
      {
        val instruction = instructions[instNum]
        val symbol = instruction.opCode.symbol
        if (symbol == Symbol.LDCINT)
          {
            instruction as InstructionOneArg
            val arg = instruction.arg.text
            val labels : MutableList<Token> = instruction.labels

            if (arg == "0")
              {
                // replace LDCINT 0 with LDCINT0
                val loadToken = Token(Symbol.LDCINT0)
                val loadInst = InstructionLDCINT0(labels, loadToken)
                instructions[instNum] = loadInst
              }
            else if (arg == "1")
              {
                // replace LDCINT 1 with LDCINT1
                val loadToken = Token(Symbol.LDCINT1)
                val loadInst = InstructionLDCINT1(labels, loadToken)
                instructions[instNum] = loadInst
              }
          }
        else if (symbol == Symbol.LDCB)
          {
            instruction as InstructionOneArg
            val arg = instruction.arg.text
            val labels : MutableList<Token> = instruction.labels

            if (arg == "0")
              {
                // replace LDCB 0 with LDCB0
                val loadToken = Token(Symbol.LDCB0)
                val loadInst = InstructionLDCB0(labels, loadToken)
                instructions[instNum] = loadInst
              }
            else if (arg == "1")
              {
                // replace LDCB 1 with LDCB1
                val loadToken = Token(Symbol.LDCB1)
                val loadInst = InstructionLDCB1(labels, loadToken)
                instructions[instNum] = loadInst
              }
          }
      }
  }