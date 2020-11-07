package edu.citadel.sel.ast


/**
 * Context is essentially a map from an identifier
 * (type String) to its value (type double).
 */
class Context
  {
    private val idValueMap = mutableMapOf<String, Double>()

    /**
     * Add the specified identifier and its associated value to the context.
     */
    fun put(identifier : String, value : Double)
      {
        idValueMap[identifier] = value
      }

    /**
     * Returns the value associated with the specified identifier.  Returns
     * 0.0 if the identifier has not previously been associated with a value.
     */
    operator fun get(identifier : String) : Double = idValueMap[identifier] ?: 0.0
  }
