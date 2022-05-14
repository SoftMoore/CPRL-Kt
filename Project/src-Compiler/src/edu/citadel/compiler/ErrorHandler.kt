package edu.citadel.compiler


import java.io.*
import kotlin.system.exitProcess


/**
 * This object handles the reporting of error messages.
 */
object ErrorHandler
  {
    private var err = PrintWriter(System.err, true)
    private var errorCount = 0


    /**
     * Maximum number of errors to be reported.
     */
    private const val MAX_ERRORS = 15


    /**
     * Sets the print writer to be used for all error messages written
     * by the error handler.  (Defaults to System.err if not set.)
     */
    fun setPrintWriter(err : PrintWriter)
      {
        this.err = err
      }


    /**
     * Returns true if errors have been reported by the error handler.
     */
    fun errorsExist() : Boolean = errorCount > 0


    /**
     * Reports the error.  Stops compilation if the maximum number of
     * errors have been reported.
     */
    fun reportError(e : CompilerException)
      {
        if (errorCount <= MAX_ERRORS)
          {
            err.println(e.message)
            ++errorCount
          }
        else
          {
            err.println("*** Max errors exceeded.  Compilation halted***")
            exitProcess(1)       // stop the compiler with a nonzero status code
          }
      }


    /**
     * Reports the error and exits compilation.
     */
    fun reportFatalError(e : Exception)
      {
        e.printStackTrace()
        exitProcess(1)       // stop the compiler with a nonzero status code
      }


    /**
     * Reports a warning and continues compilation.
     */
    fun reportWarning(warningMessage : String) = err.println("Warning: $warningMessage")


    /**
     * Reports a warning and continues compilation.
     */
    fun printMessage(message : String) = err.println(message)
  }
