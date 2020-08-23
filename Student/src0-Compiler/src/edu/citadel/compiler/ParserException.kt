package edu.citadel.compiler


/**
 * Class for exceptions encountered during parsing.
 *
 * @constructor Construct a ParserException with the specified error message and position.
 */
class ParserException(position : Position, message : String)
    : CompilerException("Syntax", position, message)
