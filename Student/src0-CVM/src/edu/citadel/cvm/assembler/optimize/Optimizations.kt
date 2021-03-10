package edu.citadel.cvm.assembler.optimize


/**
 * This object is used to retrieve the list of all optimizations.
 */
object Optimizations
  {
    val optimizations = listOf(ConstFolding(),
                               IncDec(),
                               IncDec2(),
                               ShiftLeftRight(),
                               ShiftLeft(),
                               BranchingReduction(),
                               ConstNeg(),
                               LoadSpecialConstants(),
                               Allocate(),
                               DeadCodeElimination())
  }
