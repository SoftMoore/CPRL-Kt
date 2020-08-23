package edu.citadel.cvm.assembler.optimize


import java.util.LinkedList


/**
 * This object is used to retrieve the list of all optimizations.
 */
object Optimizations
  {
    private val optimizations : MutableList<Optimization>


    fun getOptimizations() : List<Optimization>
      {
        return optimizations
      }


    /**
     * Initialize list of optimizations to be performed
     */
    init
      {
        optimizations = LinkedList()
        optimizations.add(ConstFolding())
        optimizations.add(IncDec())
        optimizations.add(IncDec2())
        optimizations.add(ShiftLeftRight())
        optimizations.add(ShiftLeft())
        optimizations.add(BranchingReduction())
        optimizations.add(ConstNeg())
        optimizations.add(LoadSpecialConstants())
      }
  }
