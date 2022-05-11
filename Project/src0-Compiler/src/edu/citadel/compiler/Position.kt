package edu.citadel.compiler


/**
 * This class encapsulates the concept of a position in a source file, where
 * the position is characterized by an ordered pair of integers: a line number
 * relative to the source file and a character number relative to that line.
 * Note: Position objects are immutable.
 *
 * @constructor Construct a position with the given line number and character number.
*/
class Position(val lineNumber : Int = 0, val charNumber : Int = 0)
  {
    override fun toString() = "line $lineNumber, character $charNumber"
  }
