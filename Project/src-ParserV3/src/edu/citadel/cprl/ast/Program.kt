package edu.citadel.cprl.ast


/**
 * The abstract syntax tree node for a CPRL program.
 *
 * @constructor Construct a program with the specified declarative
 *              and statement parts.
 */
class Program(private val declPart : DeclarativePart = DeclarativePart(),
              private val stmtPart : StatementPart   = StatementPart())
    : AST()
  {
    private var varLength = 0      // # bytes of all declared variables


    // label for first program statement (used during code generation)
    private val L1 : String = getNewLabel()


    override fun checkConstraints()
      {
        declPart.checkConstraints()
        stmtPart.checkConstraints()
      }


    /**
     * Set the relative address (offset) for each variable
     * and compute the length of all variables.
     */
    private fun setRelativeAddresses()
      {
        // initial relative address is 0 for a program
        var currentAddr = 0

        for (decl in declPart.initialDecls)
          {
            // set relative address for single variable declarations
            if (decl is SingleVarDecl)
              {
                decl.relAddr = currentAddr
                currentAddr  = currentAddr + decl.size
              }
          }

        // compute length of all variables
        varLength = currentAddr
      }


    override fun emit()
      {
        setRelativeAddresses()

        // no need to emit PROGRAM instruction if varLength == 0
        if (varLength > 0)
            emit("PROGRAM $varLength")

        // emit branch over subprograms only if necessary
        if (declPart.subprogramDecls.isNotEmpty())
          {
            // jump over code for subprograms
            emit("BR $L1")
            declPart.emit()
            emitLabel(L1)
          }
        else
            declPart.emit()

        stmtPart.emit()
        emit("HALT")
      }
  }
