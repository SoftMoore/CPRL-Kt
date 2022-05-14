package edu.citadel.compiler


/**
 * Class for exceptions encountered during scanning.
 *
 * @constructor Construct a ScannerException with the specified error message and position.
 */
class ScannerException(position : Position, message : String)
    : CompilerException("Lexical", position, message)
