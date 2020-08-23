package edu.citadel.compiler


/**
 * Superclass for all compiler exceptions.
 */
abstract class CompilerException : Exception
  {
    /**
     * Construct an exception with information about the nature and position
     * of the error.
     *
     * @param errorType the name of compilation phase in which the error was detected.
     * @param position  the position in the source file where the error was detected.
     * @param message   a brief message about the nature of the error.
     */
    constructor(errorType : String, position : Position, message : String)
        : super("*** $errorType error detected near $position:\n    $message")


    /**
     * Construct an exception with information about the nature of the error
     * but not its position.
     *
     * @param errorType the name of compilation phase in which the error was detected.
     * @param message   a brief message about the nature of the error.
     */
    constructor(errorType : String, message : String)
        : super("*** $errorType error detected: $message")
  }
