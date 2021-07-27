class TestGrid(private val size: Int) {
    var isGameOver: Boolean = false
    var isGameWon: Boolean = false
    private var score: Int = 0
    private var gameField = MutableList(size) { MutableList(size) { 0 } }

    private fun createRandomValue() = if ((1..5).random() < 5) 2 else 4
    private fun putRandomValues(times: Int) {
        var count = 0
        while (count < times) {
            val x = (0 until size).random()
            val y = (0 until size).random()
            if (gameField[x][y] == 0) {
                gameField[x][y] = createRandomValue()
                count++
            }
        }
    }

    private fun clearZeroes(line: List<Int>) = line.filter{ cell: Int -> cell > 0 }
    private fun merge(line: List<Int>, isReversed: Boolean): List<Int> {
        val mergingLine = if (isReversed) line.reversed().toMutableList() else line.toMutableList()
        for (i in 1 until mergingLine.size) {
            if (mergingLine[i] == mergingLine[i - 1]) {
                mergingLine[i] = 0
                countScore(mergingLine[i - 1])
                mergingLine[i - 1] *= 2
            }
        }
        val mergedLine = clearZeroes(mergingLine)
        return if (isReversed) mergedLine.reversed() else mergedLine
    }
    private fun countScore(value: Int) {
        score += value
    }
    private fun fillZeroes(line: List<Int>, isReversed: Boolean): List<Int> {
        return when {
            isReversed -> (line.reversed() + List(size - line.size) { 0 }).reversed()
            else -> line + List(size - line.size) { 0 }
        }
    }
    private fun inverseField(field: List<List<Int>>): MutableList<MutableList<Int>> {
        val inversedField = MutableList(size) { MutableList(size) { 0 } }
        for (i in 0 until size) {
            for (j in 0 until size) {
                inversedField[i][j] = field[j][i]
            }
        }
        return inversedField
    }
    private fun checkGameStatus(): Boolean {
        checkForWin()
        if(isGameWon) return true
        val field = inverseField(gameField)
        for (i in 0 until size) {
            val line = clearZeroes(gameField[i])
            val invertedLine = clearZeroes(field[i])
            if (line.isEmpty() || invertedLine.isEmpty()) return false
            if (line != fillZeroes(merge(line, false), false)) return false
            if (line != fillZeroes(merge(line, true), true)) return false
            if (invertedLine != fillZeroes(merge(invertedLine, false), false)) return false
            if (invertedLine != fillZeroes(merge(invertedLine, true), true)) return false

        }
        return true
    }
    private fun checkForWin() {
        for(i in 0 until size){
            for(j in 0 until size){
                if(gameField[i][j] == 2048) {
                    isGameWon = true
                    break
                }
            }
        }
    }


    fun resetGameField() {
        gameField.forEach { row -> for (i in row.indices) row[i] = 0 }
        putRandomValues(2)
        isGameOver = false
    }
    fun printGameField() {
        var bufferValue = 0
        gameField.forEach { row ->
            row.forEach { element -> if (bufferValue < element) bufferValue = element }
        }
        val width = bufferValue.toString().length + 1
        gameField.forEach { row ->
            row.forEach { element -> print("%${width}d".format(element)) }//" $element ")}
            println()
        }
        println()

    }
    fun showScore() = score
    fun move(direction: String){
        var count = 0
        val field = if(direction == "w" || direction == "s") inverseField(gameField) else gameField
        for (i in 0 until size) {
            var line = clearZeroes(field[i])
            if (line.isNotEmpty()) {
                when (direction) {
                    "w", "a" -> line = fillZeroes(merge(line, false), false)
                    "s", "d" -> line = fillZeroes(merge(line, true), true)
                }
                if (field[i] == line.toMutableList()) count++
                else field[i] = line.toMutableList()
            } else count++
        }
        gameField = if(direction == "w" || direction == "s") inverseField(field) else field
        if (count < size) putRandomValues(1)
        isGameOver = checkGameStatus()
    }
}