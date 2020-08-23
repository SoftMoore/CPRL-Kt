package edu.citadel.cprl


import edu.citadel.compiler.ParserException


private const val INITIAL_SCOPE_LEVELS = 2
private const val INITIAL_MAP_SIZE = 50


/**
 * The types for identifiers stored in the identifier table.
 */
enum class IdType
  {
    constantId, variableId, arrayTypeId, procedureId, functionId
  }


/**
 * A simplified version of an identifier table (also known as a symbol table)
 * used to hold attributes of identifiers in the programming language CPRL.
 */
class IdTable
  {
    // NOTE: IdTable is implemented as a stack of maps, where each map associates
    // the identifier string with its IdType.  The stack is implemented using an
    // array list.  When a when a new scope is opened, a new map is pushed onto the
    // stack.  Searching for a declaration involves searching at the current level
    // (top map in the stack) and then at enclosing scopes (maps under the top).

    private val table : ArrayList<MutableMap<String, IdType>>
    private var currentLevel : Int


    /**
     * Initialize an empty identifier table with scope level initialized to 0.
     */
    init
      {
        table = ArrayList(INITIAL_SCOPE_LEVELS)
        currentLevel = 0
        table.add(currentLevel, HashMap(INITIAL_MAP_SIZE))
      }


    /**
     * Opens a new scope for identifiers.
     */
    fun openScope()
      {
        ++currentLevel
        table.add(currentLevel, HashMap(INITIAL_MAP_SIZE))
      }

    /**
     * Closes the outermost scope.
     */
    fun closeScope()
      {
        table.removeAt(currentLevel)
        --currentLevel
      }


    /**
     * Add a token and its type at the current scope level.
     *
     * @throws ParserException if the identifier token is already defined in the current scope.
     */
    fun add(idToken : Token, idType : IdType)
      {
        // assumes that idToken is an identifier token
        assert(idToken.symbol == Symbol.identifier)
          { "IdTable.add(): The symbol for idToken is not an identifier." }
        val idMap = table[currentLevel]
        val oldDecl = idMap.put(idToken.text, idType)

        // check that the identifier has not been defined previously
        if (oldDecl != null)
          {
            val errorMsg = ("Identifier \"${idToken.text} \" is "+
                    "already defined in the current scope.")
            throw ParserException(idToken.position, errorMsg)
          }
      }

    /**
     * Returns the IdType associated with the identifier token's text.
     * Returns null if the identifier is not found.  Searches enclosing
     * scopes if necessary.
     */
    operator fun get(idToken : Token) : IdType?
      {
        // assumes that idToken is an identifier token
        assert(idToken.symbol == Symbol.identifier)
          { "IdTable.get(): The symbol for idToken is not an identifier." }

        var idType : IdType? = null
        var level = currentLevel
        while (level >= 0 && idType == null)
          {
            val idMap : Map<String, IdType> = table[level]
            idType = idMap[idToken.text]
            --level
          }
        return idType
      }
  }