package edu.citadel.compiler.util


/*
 * Utilities for recognizing binary and hexadecimal digits.
 */


/**
 * Returns true only if the specified character is a binary digit ('0' or '1').
 */
fun isBinaryDigit(ch : Char) : Boolean
    = ch == '0' || ch == '1'


/**
 * Returns true only if the specified character is a hex digit
 * ('0'..'9', 'A'..'F', or 'a'..'f').
 */
fun isHexDigit(ch : Char) : Boolean
    = (ch in '0'..'9') || (ch in 'a'..'f') || (ch in 'A'..'F')
