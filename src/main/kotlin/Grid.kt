class Grid(private val size: Int) {
    private var _isGameOver = false
    private var _isGameWon = false
    private var emptySlots = true
    private var _score = 0
    val score get() = _score
    val isGameOver get() = _isGameOver
    val isGameWon get() = _isGameWon

    private var gameField = MutableList(size) { MutableList(size) { 0 } }
    private val randomValue get() = if ((1..5).random() < 5) 2 else 4

    private fun putRandomValues(times: Int) {
        //Every move ends with the new value inserted into gameField
        //The method creates random coordinates and if the cell in these coordinate is empty
        //it will put randomValue there
        var count = 0
        while (count < times) {
            val (x, y) = Pair((0 until size).random(), (0 until size).random())
            if (gameField[x][y] == 0) {
                gameField[x][y] = randomValue
                count++
            }
        }
    }

    private fun checkAvailableMoves(field: MutableList<MutableList<Int>>): Boolean {
        if (field.any { row -> row.any { it == 0 } }) {
            //If at least one cell is empty in the gameField
            //it means that there are definitely available moves
            emptySlots = true
            return true
        }
        else {
            //However, lack of empty cells doesn't necessarily mean no available moves.
            //Therefore it must also check, whether there are any mergeable cells
            emptySlots = false
            repeat(2) {
                //This part is performed two times, one while gameField isn't transposed
                //and another where it is
                field.forEach { it.zipWithNext { first, second -> if (first == second) return true } }
                //This part basically zips neighboring cells together. By definition, cell is considered
                //mergeable if it has either horizontal or vertical neighbor with the same value (thus, transposing)
                //it takes at least one mergeable for available move to exist.
                field.transpose()
            }
        }
        return false
    }

    private fun MutableList<Int>.merge(isReversed: Boolean, length: Int) {
            this.removeAll { it == 0 }
            if (isReversed) this.reverse()
            for (i in 1 until this.size) {
                if (this[i] == this[i - 1]) {
                    this[i - 1] *= 2
                    _score += this[i - 1]
                    this.removeAt(i)

                    this.add(0)
                }
            }
            this.removeAll { it == 0 }
            this.addAll(List(length - size) { 0 })
            if (isReversed) this.reverse()
    }

    private fun MutableList<MutableList<Int>>.transpose() {
        //The need for transposing gameField arises from the fact that
        //the map is not a 2-D list but rather list of lists.
        //Any operation on a row is an operation on a list,
        //however that doesn't translate to columns, since the column is collection
        //of values at certain position in a list from every list in gameField.
        //So, in order to utilize the merge method on a column the same way as on a row without complicating it,
        //this method basically flips gameField on a side, converting columns into rows(definition of transposing)
        val temp = MutableList(size) { MutableList(size) { 0 } }
        for (i in 0 until size) {
            for (j in 0 until size) {
                temp[i][j] = this[i][j]
            }
        }
        for (i in 0 until size) {
            for (j in 0 until size) {
                this[j][i] = temp[i][j]
            }
        }
    }

    fun resetGameField() {
        gameField.forEach { it.map { List(size) { 0 } } }
        putRandomValues(2)
        emptySlots = true
        _isGameOver = false
        _isGameWon = false
    }

    fun printGameField() {
        //val width is used to determine the largest number in the grid to correctly print grid without shifting.
        val width = gameField.flatten().maxByOrNull { it }.toString().length + 1
        gameField.forEach { row -> row.forEach { print("%${width}d".format(it)) }; println() }
        println()
    }

    fun move(direction: String) {
        val isReversed = direction == "s" || direction == "d"
        val isVertical = direction == "w" || direction == "s"
        if (isVertical) gameField.transpose()
        gameField.map { it.merge(isReversed, size) }
        if (isVertical) gameField.transpose()
        _isGameWon = gameField.any { row -> row.any { it == 2048 } }
        _isGameOver = !checkAvailableMoves(gameField) || _isGameWon
        if (emptySlots) putRandomValues(1)
    }
}