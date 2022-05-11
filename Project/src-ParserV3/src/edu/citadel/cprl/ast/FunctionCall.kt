package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Token


/**
 * The abstract syntax tree node for a function call expression.
 *
 * @constructor Construct a function call with the function identifier
 *              (name), the list of actual parameters, and a reference
 *              to the function declaration.
 */
class FunctionCall(private val funcId       : Token,
                   private val actualParams : List<Expression>,
                   private val funcDecl     : FunctionDecl)
    : Expression(funcDecl.type, funcId.position)
  {
    override fun checkConstraints()
      {
        try
          {
            val formalParams = funcDecl.formalParams

            // check that numbers of parameters match
            if (actualParams.size != formalParams.size)
                throw error(funcId.position, "Incorrect number of actual parameters.")

            // call checkConstraints for each actual parameter
            for (expr in actualParams)
                expr.checkConstraints()

            // check that parameter types match
            for (i in actualParams.indices)
              {
                val expr  = actualParams[i]
                val param = formalParams[i]

                if (!matchTypes(expr.type, param.type))
                    throw error(expr.position, "Parameter type mismatch.")
              }
          }
        catch (e : ConstraintException)
          {
            ErrorHandler.reportError(e)
          }
      }


    override fun emit()
      {
        // allocate space on the stack for the return value
        emit("ALLOC ${funcDecl.type.size}")

        // emit code for actual parameters
        for (expr in actualParams)
            expr.emit()

        emit("CALL ${funcDecl.subprogramLabel}")
      }
  }
