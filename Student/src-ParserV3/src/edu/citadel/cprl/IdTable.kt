package edu.citadel.cprl


import edu.citadel.compiler.ParserException
import edu.citadel.cprl.ast.Declaration


private const val INITIAL_SCOPE_LEVELS = 2
private const val INITIAL_MAP_SIZE = 50


/**
 * The identifier table (also known as a symbol table) is used to
 * hold attributes of identifiers in the programming language CPRL.
 */
class IdTable
  {
    private val table : ArrayList<HashMap<String, Declaration>> = ArrayList(INITIAL_SCOPE_LEVELS)
    private var currentLevel : Int = 0


    /**
     * Initialize an empty identifier table with scope level initialized to 0.
     */
    init
      {
        currentLevel = 0
        table.add(currentLevel, HashMap())
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
     * Add a declaration at the current scope level.
     *
     * @throws ParserException if the identifier token associated with the
     *                         declaration is already defined in the current scope.
     */
    fun add(decl : Declaration)
      {
        val idToken = decl.idToken

        // assumes that idToken is an identifier token
        assert(idToken.symbol == Symbol.identifier)
          { "IdTable.add(): The symbol for idToken is not an identifier." }

        val idMap = table[currentLevel]
        val oldDecl = idMap.put(idToken.text, decl)

        // check that the identifier has not been defined previously
        if (oldDecl != null)
          {
            val errorMsg = "Identifier \"${idToken.text} \" is " +
                            "already defined in the current scope."
            throw ParserException(idToken.position, errorMsg)
          }
      }


    /**
     * Returns the Declaration associated with the identifier token's text.
     * Returns null if the identifier is not found.  Searches enclosing
     * scopes if necessary.
     */
    operator fun get(idToken : Token) : Declaration?
     {
        // assumes that idToken is an identifier token
        assert(idToken.symbol == Symbol.identifier)
          { "IdTable.get(): The symbol for idToken is not an identifier." }

        var decl: Declaration? = null
        var level = currentLevel

        while (level >= 0 && decl == null)
          {
            val idMap = table[level]
            decl = idMap[idToken.text]
            --level
          }

        return decl
      }


    /**
     * Returns the current scope level.
     */
    fun getCurrentLevel() : ScopeLevel =
            if (currentLevel == 0) ScopeLevel.PROGRAM else ScopeLevel.SUBPROGRAM
  }
