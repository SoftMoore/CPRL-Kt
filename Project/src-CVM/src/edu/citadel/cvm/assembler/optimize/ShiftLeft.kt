package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token
import edu.citadel.cvm.assembler.ast.*


/**
 * Replaces multiplication by a power of 2 times a variable with left
 * shift.  Basically, this class looks for patterns of the form
 * "LDCINT 2**n, LDLADDR x, LOADW, MUL" and replaces it with
 * "LDLADDR x, LOADW, SHL n".  Note that the analogous replacement
 * for division will not work since division is not commutative.
 */
class ShiftLeft : Optimization
  {
    override fun optimize(instructions : MutableList<Instruction>, instNum : Int)
      {
        // quick check that there are at least four instructions remaining
        if (instNum > instructions.size - 4)
            return

        val instruction0 = instructions[instNum]
        val instruction1 = instructions[instNum + 1]
        val instruction2 = instructions[instNum + 2]
        val instruction3 = instructions[instNum + 3]

        // quick check that we are dealing with a constant and a variable
        val symbol0 = instruction0.opCode.symbol
        val symbol1 = instruction1.opCode.symbol
        val symbol2 = instruction2.opCode.symbol

        // quick check that we have LDCINT, LDLADDR, and LOADW
        if (symbol0 != Symbol.LDCINT || symbol1 != Symbol.LDLADDR || symbol2 != Symbol.LOADW)
              return

        instruction0 as InstructionOneArg
        val arg0 = instruction0.arg.text
        val shiftAmount = getShiftAmount(Integer.parseInt(arg0)).toInt()

        if (shiftAmount > 0)
          {
            val symbol3 = instruction3.opCode.symbol

            if (symbol3 == Symbol.MUL)
              {
                // replace MUL by SHL
                val shlToken = Token(Symbol.SHL)
                val labels   = instruction3.labels
                val argStr   = shiftAmount.toString()
                val argToken = Token(Symbol.intLiteral, argStr)
                val shlInst  = InstructionSHL(labels, shlToken, argToken)
                instructions[instNum + 3] = shlInst
              }
            else
                return

            // copy labels from inst0 to inst1 before removing it
            val inst1Labels = instruction1.labels
            inst1Labels.addAll(instruction0.labels)

            // remove the LDCINT instruction
            instructions.removeAt(instNum)
          }
      }
  }
