package edu.citadel.sel


import edu.citadel.compiler.Position
import edu.citadel.sel.ast.*
import java.io.IOException
import java.util.*


/**
 * This class uses recursive descent to perform syntax analysis of the SEL
 * source language and to generate an abstract syntax tree for the interpreter.
 *
 * @constructor Construct a parser with the specified scanner.
 */
class Parser(private val scanner : Scanner)
{
    /**
     * Parse the following grammar rule:<br></br>
     * `program = ( statement )* .`
     *
     * @return the parsed program Expression
     */
    @Throws(IOException::class, InterpreterException::class)
    fun parseProgram() : Program
      {
        val expressions = ArrayList<Expression>()

        while (scanner.symbol !== Symbol.EOF)
          {
            val expression = parseExpression()
            if (expression != null)
                expressions.add(expression)
          }

        return Program(expressions)
      }


    /**
     * Parse the following grammar rule:<br></br>
     * `expression = assignExpr | numericExpr  ( EOL )?.`
     *
     * @return the parsed Expression
     */
    @Throws(IOException::class, InterpreterException::class)
    fun parseExpression() : Expression?
      {
        val returnExpr = if (scanner.symbol == Symbol.identifier && scanner.peekSymbol == Symbol.assign)
                             parseAssignExpr()
                         else
                             parseNumericExpr()

        if (scanner.symbol == Symbol.EOL)
            match(Symbol.EOL)

        return returnExpr
      }

    /**
     * Parse the following grammar rule:<br></br>
     * `assignExpr = identifier "<-" numericExpr .`
     *
     * @return the parsed assign Expression
     */
    @Throws(IOException::class, InterpreterException::class)
    fun parseAssignExpr() : Expression?
      {
        assert(scanner.symbol == Symbol.identifier && scanner.peekSymbol == Symbol.assign)
        val idToken = scanner.token
        scanner.advance()
        match(Symbol.assign)
        val expr : Expression? = parseExpression()

        return if (expr != null) AssignExpr(idToken, expr) else null
      }


    /**
     *
     * Parse the following grammar rules:<br></br>
     * `numericExpr = ( addOp )? term (addOp term)* .`
     * addOp = "+" | "-" .
     *
     * @return the parsed numeric Expression
     */
    @Throws(IOException::class, InterpreterException::class)
    fun parseNumericExpr() : Expression?
      {
        var expr : Expression?
        var operator : Token? = null

        if (scanner.symbol.isAddingOperator)
          {
            if (scanner.symbol == Symbol.minus)
                operator = scanner.token

            // leave operator as null if it is "+"
            matchCurrentSymbol()
          }

        expr = parseTerm()

        if (operator != null && expr != null)
            expr = NegationExpr(operator, expr)

        while (scanner.symbol.isAddingOperator)
          {
            operator = scanner.token
            matchCurrentSymbol()
            val expr2 : Expression? = parseTerm()

            if (expr != null && expr2 != null)
                expr = AddingExpr(expr, operator, expr2)
          }

        return expr
      }

    /**
     * Parse the following grammar rules:<br></br>
     * `term = factor ( multiplyingOp factor )* .`
     * multiplyingOp = "*" | "/".
     *
     * @return the parsed term Expression
     */
    @Throws(IOException::class, InterpreterException::class)
    fun parseTerm() : Expression?
      {
        var expr : Expression? = parseFactor()

        while (scanner.symbol.isMultiplyingOperator)
          {
            val operator = scanner.token
            matchCurrentSymbol()
            val expr2 : Expression? = parseFactor()
            if (expr != null && expr2 != null)
                expr = MultiplyingExpr(expr, operator, expr2)
          }

        return expr
      }

    /**
     * Parse the following grammar rule:<br></br>
     * `factor = numericLiteral | identifier | "(" expression ")" .`
     *
     * @return the parsed factor Expression
     */
    @Throws(IOException::class, InterpreterException::class)
    fun parseFactor() : Expression?
      {
        var expr : Expression? = null

        if (scanner.symbol == Symbol.numericLiteral)
            expr = parseNumericLiteral()
        else if (scanner.symbol == Symbol.identifier)
            expr = parseIdExpr()
        else if (scanner.symbol == Symbol.leftParen)
          {
            matchCurrentSymbol()
            expr = parseNumericExpr()
            match(Symbol.rightParen)
          }
        else
            error("Invalid expression.")

        return expr
      }


    /**
     * Parse a numeric literal expression.
     *
     * @return the parsed numeric literal Expression
     */
    @Throws(IOException::class, InterpreterException::class)
    fun parseNumericLiteral() : Expression
      {
        assert(scanner.symbol === Symbol.numericLiteral)
        val expr : Expression = NumericLiteral(scanner.token)
        matchCurrentSymbol()
        return expr
      }


    /**
     * Parse an identifier expression.
     *
     * @return the parsed identifier Expression
     */
    @Throws(IOException::class, InterpreterException::class)
    fun parseIdExpr() : Expression
      {
        assert(scanner.symbol === Symbol.identifier)
        val expr : Expression = IdExpr(scanner.token)
        matchCurrentSymbol()
        return expr
      }


    /**
     * Check that the current scanner symbol is the expected symbol.
     * If it is, then advance the scanner.  Otherwise, signal an error.
     */
    @Throws(IOException::class, InterpreterException::class)
    private fun match(expectedSymbol : Symbol)
      {
        if (scanner.symbol == expectedSymbol)
            scanner.advance()
        else
          {
            val errorMsg = ("Expecting \"" + expectedSymbol + "\" but found \""
                    + scanner.symbol + "\" instead.")
            error(errorMsg)
          }
      }


    /**
     * Advance the scanner.  This method represents an unconditional
     * match with the current scanner symbol.
     */
    @Throws(IOException::class, InterpreterException::class)
    private fun matchCurrentSymbol()
      {
        scanner.advance()
      }


    /**
     * Throws an exception with an appropriate error message.
     */
    @Throws(InterpreterException::class)
    private fun error(message : String)
      {
        val position : Position = scanner.position
        val errorMsg = """*** Syntax error detected near position $position:"
                     + "\n   $message"""
        throw InterpreterException(errorMsg)
      }
  }
