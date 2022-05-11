package edu.citadel.cprl


import edu.citadel.cprl.ast.LoopStmt
import java.util.Stack


/**
 * This class provides access to an enclosing loop context required
 * by exit statements for constraint analysis code generation.
 */
class LoopContext
  {
    private val loopStack : Stack<LoopStmt> = Stack()

    /**
     * The loop statement currently being parsed; null if not currently parsing a loop.
     */
    val loopStmt : LoopStmt?
        get() = if (loopStack.empty()) null else loopStack.peek()

    /**
     * Called when starting to parse a loop statement.
     */
    fun beginLoop(stmt : LoopStmt)
      {
        loopStack.push(stmt)
      }


    /**
     * Called when finished parsing a loop statement.
     */
    fun endLoop()
      {
        if (!loopStack.empty())
            loopStack.pop()
      }
  }
