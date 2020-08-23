package edu.citadel.compiler.util


/*
 * Utilities for recognizing binary and hexadecimal digits.
 */


/**
 * Returns true only if the specified character is a binary digit ('0' or '1').
 */
fun isBinaryDigit(ch : Char) = ch == '0' || ch == '1'


/**
 * Returns true only if the specified character is a hex digit
 * ('0'-'9', 'A'..'F', or 'a'..'f').
 */
fun isHexDigit(ch : Char) = Character.isDigit(ch)
                         || Character.toUpperCase(ch) in 'A'..'F'
