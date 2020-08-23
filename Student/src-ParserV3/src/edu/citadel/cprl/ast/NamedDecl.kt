package edu.citadel.cprl.ast


import edu.citadel.cprl.ScopeLevel
import edu.citadel.cprl.Type


/**
 * Interface for a named declaration, which can be either a
 * "single" variable declaration or a parameter declaration.
 */
interface NamedDecl
  {
    /**
     * The type of this named declaration.
     */
    val type : Type


    /**
     * The size (number of bytes) of the variable declared with this named declaration.
     */
    val size : Int


    /**
     * The scope level for this declaration.
     */
    val scopeLevel : ScopeLevel


    /**
     * The relative address (offset) of the variable declared with this named declaration.
     */
    var relAddr : Int
  }
