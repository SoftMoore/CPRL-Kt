package edu.citadel.cprl.ast


/**
 * The abstract syntax tree node for a named value.  A named value is similar
 * to a variable except that it generates different code.  For example, consider
 * the assignment statement "x := y;"  The identifier "x" represents a variable,
 * and the identifier "y" represents a named value.  Code generation for "x"
 * would leave its address on the top of the stack, while code generation for
 * "y" would leave its value on the top of the stack.
 *
 * @constructor Construct a named value from a variable.
 */
class NamedValue(variable : Variable)
    : Variable(variable.decl, variable.position, variable.indexExprs)
  {
    override fun emit()
      {
        super.emit()         // leaves address of variable on top of stack
        emitLoadInst(type)   // replaces address by value at that address
      }
  }
