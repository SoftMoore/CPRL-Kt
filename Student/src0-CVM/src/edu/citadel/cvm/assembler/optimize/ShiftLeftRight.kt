package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token
import edu.citadel.cvm.assembler.ast.*


/**
 * Replaces multiplication by a power of 2 with left shift and
 * division by a power of two with right shift where possible.
 */
class ShiftLeftRight : Optimization
  {
    override fun optimize(instructions : MutableList<Instruction>, instNum : Int)
      {
        // quick check that there are at least 2 instructions remaining
        if (instNum > instructions.size - 2)
            return

        val instruction0 = instructions[instNum]
        val instruction1 = instructions[instNum + 1]

        val symbol0 = instruction0.opCode.symbol

        if (symbol0 == Symbol.LDCINT)
          {
            instruction0 as InstructionOneArg
            val shiftAmount = getShiftAmount(instruction0.argToInt())

            if (shiftAmount > 0)
              {
                // make sure that inst1 does not have any labels
                val inst1Labels = instruction1.labels
                if (inst1Labels.isEmpty())
                  {
                    val symbol1 = instruction1.opCode.symbol

                    if (symbol1 == Symbol.MUL)
                      {
                        // replace LDCINT with SHL
                        val shlToken = Token(Symbol.SHL)
                        val labels   = instruction0.labels
                        val argStr   = shiftAmount.toString()
                        val argToken = Token(Symbol.intLiteral, argStr)
                        val shlInst  = InstructionSHL(labels, shlToken, argToken)
                        instructions[instNum] = shlInst
                      }
                    else if (symbol1 == Symbol.DIV)
                      {
                        // replace LDCINT by SHR
                        val shrToken = Token(Symbol.SHR)
                        val labels   = instruction0.labels
                        val argStr   = shiftAmount.toString()
                        val argToken = Token(Symbol.intLiteral, argStr)
                        val shrInst  = InstructionSHR(labels, shrToken, argToken)
                        instructions[instNum] = shrInst
                      }
                    else
                        return

                    // remove the MUL/DIV instruction
                    instructions.removeAt(instNum + 1)
                  }
              }
          }
      }
  }
