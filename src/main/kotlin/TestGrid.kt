class TestGrid(private val size: Int) {
    private var _isGameOver = false
    private var _isGameWon = false
    private var _score = 0
    val score get() = _score
    val isGameOver get() = _isGameOver
    val isGameWon get() = _isGameWon

    private val gameField = MutableList(size) { MutableList(size) { 0 } }
    private val randomValue get() = if ((1..5).random() < 5) 2 else 4

    private fun putRandomValues(times: Int) {
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
        if (field.any { row -> row.any { it == 0 } }) return true
        repeat(2) {
            field.forEach { it.zipWithNext { first, second -> if (first == second) return true } }
            field.transpose()
        }
        return false
    }

    private fun MutableList<Int>.merge(isReversed: Boolean, length: Int) {
        if (this.any { it > 0 }) {
            this.removeAll { it == 0 }
            if (isReversed) this.reverse()
            for (i in 1 until this.size) {
                if (this[i] == this[i - 1]) {
                    _score += this[i - 1]
                    this[i - 1] *= 2
                    this[i] = 0
                }
            }
            this.removeAll { it == 0 }
            this.addAll(List(length - size) { 0 })
            if (isReversed) this.reverse()
        }
    }

    private fun MutableList<MutableList<Int>>.transpose() {
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
        _isGameOver = false
        _isGameWon = false
    }

    fun printGameField() {
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
        putRandomValues(1)
    }
}