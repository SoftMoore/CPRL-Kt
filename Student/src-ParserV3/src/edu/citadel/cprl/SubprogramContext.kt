package edu.citadel.cprl


import edu.citadel.cprl.ast.SubprogramDecl


/**
 * This class provides access to an enclosing subprogram context required
 * by return statements for constraint analysis and code generation.
 */
class SubprogramContext
  {
    /**
     * The subprogram declaration currently being parsed;
     * null if not currently parsing a subprogram.
     */
    var subprogramDecl : SubprogramDecl? = null
        private set


    /**
     * Called when starting to parse a subprogram declaration.
     */
    fun beginSubprogramDecl(subprogDecl : SubprogramDecl)
      {
        this.subprogramDecl = subprogDecl
      }


    /**
     * Called when finished parsing a subprogram declaration.
     */
    fun endSubprogramDecl()
      {
        subprogramDecl = null
      }
  }
