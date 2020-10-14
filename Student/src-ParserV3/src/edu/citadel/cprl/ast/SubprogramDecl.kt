package edu.citadel.cprl.ast


import edu.citadel.cprl.Token
import edu.citadel.cvm.Constants


/**
 * Base class for CPRL procedures and functions.
 *
 * @constructor Construct a subprogram declaration with the specified subprogram identifier.
 */
abstract class SubprogramDecl(subprogramId : Token) : Declaration(subprogramId)
  {
    /**
     * The list of formal parameters for this subprogram.
     */
    var formalParams : List<ParameterDecl> = ArrayList()

    /**
     * The list of initial declarations for this subprogram
     */
    lateinit var initialDecls : List<InitialDecl>

    /**
     * The statement part for this subprogram.
     */
    lateinit var stmtPart : StatementPart

    /**
     * The number of bytes required for all variables in the initial declarations.
     */
    protected var varLength = 0

    /**
     * The label associated with the first statement of the subprogram.
     */
    val subprogramLabel : String = getNewLabel()

    /**
     * Returns the number of bytes for all parameters.
     */
    val paramLength : Int
        get()
          {
            var paramLength = 0

            for (decl in formalParams)
                paramLength += decl.size

            return paramLength
          }


    override fun checkConstraints()
      {
        for (decl in initialDecls)
            decl.checkConstraints()

        for (paramDecl in formalParams)
            paramDecl.checkConstraints()

        stmtPart.checkConstraints()
      }


    /**
     * Set the relative address (offset) for each variable and
     * parameter, and compute the length of all variables.
     */
    protected fun setRelativeAddresses()
      {
        // initial relative address for a subprogram
        var currentAddr = Constants.BYTES_PER_FRAME

        for (decl in initialDecls)
          {
            // set relative address for single variable declarations
            if (decl is SingleVarDecl)
              {
                decl.relAddr = currentAddr
                currentAddr += decl.size
              }
          }

        // compute length of all variables by subtracting initial relative address
        varLength = currentAddr - Constants.BYTES_PER_FRAME

        // set relative address for parameters
        if (formalParams.isNotEmpty())
          {
            // initial relative address for a subprogram parameter
            currentAddr = 0

            // we need to process the parameter declarations in reverse order
            val iter = formalParams.listIterator(formalParams.size)
            while (iter.hasPrevious())
              {
                val decl = iter.previous()
                currentAddr = currentAddr - decl.size
                decl.relAddr = currentAddr
              }
          }
      }
  }
