package edu.citadel.compiler


/**
 * Class for exceptions encountered during code generation.
 */
class CodeGenException : CompilerException
  {
    /**
     * Construct a CodeGenException with the specified error message and position.
     */
    constructor (position: Position, message: String)
        : super("Code Generation", position, message)


    /**
     * Construct a CodeGenException with the specified error message.
     */
    constructor(message : String) : super("Code Generation", message)
  }
