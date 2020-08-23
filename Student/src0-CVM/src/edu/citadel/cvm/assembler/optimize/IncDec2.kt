package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token
import edu.citadel.cvm.assembler.ast.*


/**
 * Replaces addition of 1 to a variable with increment and subtraction
 * of 1 from a variable with decrement.  Basically, this class looks for
 * patterns of the form "LDCINT 1, LDLADDR x, LOADW, ADD" and replaces it
 * with "LDLADDR x, LOADW, INC", and similarly for SUB.
 */
class IncDec2 : Optimization
  {
    override fun optimize(instructions : MutableList<Instruction>, instNum :  Int)
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

        if (arg0 == "1")
          {
            val symbol3 = instruction3.opCode.symbol

            if (symbol3 == Symbol.ADD)
              {
                // replace ADD with INC
                val incToken = Token(Symbol.INC)
                val labels = instruction3.labels
                val incInst = InstructionINC(labels, incToken)
                instructions[instNum + 3] = incInst
              }
            else if (symbol3 == Symbol.SUB)
              {
                // replace SUB with DEC
                val decToken = Token(Symbol.DEC)
                val labels   = instruction3.labels
                val decInst  = InstructionDEC(labels, decToken)
                instructions[instNum + 3] = decInst
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
