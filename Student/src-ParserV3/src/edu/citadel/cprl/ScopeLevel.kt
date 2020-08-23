package edu.citadel.cprl


/**
 * An enum class for the two declaration scope levels in CPRL.
 */
enum class ScopeLevel(val text : String)
  {
    PROGRAM("Program"),
    SUBPROGRAM("Subprogram");


    /**
     * Returns a "nice" string for the name of the scope level.  For
     * example, this method returns "Program" instead of PROGRAM.
     */
    override fun toString() : String
      {
        return text
      }
  }
