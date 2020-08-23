package edu.citadel.cvm.assembler.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cvm.assembler.optimize.Optimizations


/**
 * This class implements the abstract syntax tree for an assembly language program.
 */
class Program : AST()
  {
    private val instructions : ArrayList<Instruction> = ArrayList()


    fun addInstruction(inst : Instruction)
      {
        instructions.add(inst)
      }


    fun getInstructions() : List<Instruction>
      {
        return instructions
      }


    override fun checkConstraints()
      {
        for (inst in instructions)
            inst.checkConstraints()
      }


    /**
     * Perform code transformations that improve performance.  This method
     * is normally called after checkConstraints() and before emit();
     */
    fun optimize()
      {
        val optimizations = Optimizations.getOptimizations()

        var n = 0

        while (n < instructions.size)
         {
            for (optimization in optimizations)
                optimization.optimize(instructions, n)

            ++n
          }
      }


    /**
     * Sets the starting memory address for each instruction and defines label
     * addresses.  Note: This method should be called after optimizations have
     * been performed and immediately before code generation.
     */
    fun setAddresses()
      {
        // the starting address for the first instruction
        var address = 0

        for (inst in instructions)
          {
            try
              {
                inst.address = address
                address += inst.size
              }
            catch (e : ConstraintException)
              {
                ErrorHandler.reportError(e)
              }
          }
      }


    override fun emit()
      {
        for (inst in instructions)
            inst.emit()
      }


    override fun toString() : String
      {
        val buffer = StringBuffer(1000)

        for (inst in instructions)
            buffer.append(inst.toString())
                  .append("\n")

        return buffer.toString()
      }
  }
