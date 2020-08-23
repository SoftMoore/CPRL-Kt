package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token
import edu.citadel.cvm.assembler.ast.*


/**
 * Replaces addition of 1 with increment and subtraction of 1 with decrement.
 * Basically, this class looks for patterns of the form "LDCINT 1, ADD" and
 * replaces it with "INC", and similarly for SUB.
 */
class IncDec : Optimization
  {
    override fun optimize(instructions : MutableList<Instruction>, instNum : Int)
      {
        // quick check that there are at least 2 instructions remaining
        if (instNum > instructions.size - 2)
            return

        val instruction0 = instructions[instNum]
        val instruction1 = instructions[instNum + 1]

        val symbol0 = instruction0.opCode.symbol

        if (symbol0 != Symbol.LDCINT)
            return

        instruction0 as InstructionOneArg
        val arg0 = instruction0.arg.text

        if (arg0 == "1")
          {
            // Make sure that instruction1 does not have any labels
            val inst1Labels = instruction1.labels
            if (inst1Labels.isEmpty())
              {
                val symbol1 = instruction1.opCode.symbol

                if (symbol1 == Symbol.ADD)
                  {
                    // replace LDCINT by INC
                    val incToken = Token(Symbol.INC)
                    val labels = instruction0.labels
                    val incInst = InstructionINC(labels, incToken)
                    instructions[instNum] = incInst
                  }
                else if (symbol1 == Symbol.SUB)
                  {
                    // replace LDCINT 1 by DEC
                    val decToken = Token(Symbol.DEC)
                    val labels = instruction0.labels
                    val decInst = InstructionDEC(labels, decToken)
                    instructions[instNum] = decInst
                  }
                else
                    return

                // remove the ADD/SUB instruction
                instructions.removeAt(instNum + 1)
              }
          }
      }
  }
