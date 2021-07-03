class TestGrid(private val size: Int) {
        var isGameOver: Boolean = false
        private var moveCount: Int = 0
        private var score: Int = 0
        private var bestScore: Int = 0
        private var gameField = MutableList(size){MutableList(size) {0} }

        private fun createRandomValue() = if((1..5).random() < 5) 2 else 4
        private fun putRandomValues(times: Int){
            var count = 0
            while(count < times){
                val x = (0 until size).random()
                val y = (0 until size).random()
                if(gameField[x][y] == 0) {
                    gameField[x][y] = createRandomValue()
                    count++
                }
            }
        }
        private fun clearZeroes(line: List<Int>) = line.filter{cell: Int -> cell > 0}
        private fun merge(line: List<Int>, isReversed: Boolean): List<Int> {
                val mergedLine = (if (isReversed) line.reversed() else line).toMutableList()
                for (i in 1 until mergedLine.size) {
                    if (mergedLine[i] == mergedLine[i - 1]) {
                        mergedLine[i] = 0
                        mergedLine[i - 1] *= 2
                    }
                }
                return mergedLine.filter { cell: Int -> cell > 0 }
        }
        private fun fillZeroes(line: List<Int>, isReversed: Boolean): List<Int> {
            return when (line.size) {
                size -> {
                    line
                }
                else -> {
                    val baseLine: MutableList<Int> = MutableList(size) {0}
                    val shift: Int = if (isReversed) size - line.size else 0
                    for (i in line.indices) {
                        baseLine[i + shift] = line[i]
                    }
                    baseLine
                }
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
            val field = inverseField(gameField)
            for (i in 0 until size) {
                val line = clearZeroes(gameField[i])
                val inversedLine = clearZeroes(field[i])
                if (line.isEmpty() || inversedLine.isEmpty()) return false
                if (line != fillZeroes(merge(line, false), false)) return false
                if (line != fillZeroes(merge(line, true), true)) return false
                if (inversedLine != fillZeroes(merge(inversedLine, false), false)) return false
                if (inversedLine != fillZeroes(merge(inversedLine, true), true)) return false

            }
            return true
        }

        fun resetGameField(){
            gameField.forEach {row -> for(i in row.indices) row[i] = 0}
            putRandomValues(2)
            isGameOver = false
        }
        fun printGameField(){
            var bufferValue = 0
            gameField.forEach{row ->
                row.forEach{element -> if(bufferValue < element) bufferValue = element} }
            var width = bufferValue.toString().length + 1
            gameField.forEach{row ->
                row.forEach{element -> print("%${width}d".format(element))}//" $element ")}
                println()
            }
            println()

        }
        fun moveLeft(){
            var count = 0
            for (i in 0 until size){
                var line = clearZeroes(gameField[i])
                if (line.isNotEmpty()){
                    line = fillZeroes(merge(line, false), false)
                    if(gameField[i] == line.toMutableList()) count++
                    else gameField[i] = line.toMutableList()
                }
            }
            if(count < size) putRandomValues(1)
//            if(count == size)
                isGameOver = checkGameStatus()
        }
        fun moveRight(){
            var count = 0
            for (i in 0 until size){
                var line = clearZeroes(gameField[i])
                if (line.isNotEmpty()){
                    line = fillZeroes(merge(line, true), true)
                    if(gameField[i] == line.toMutableList()) count++
                    else gameField[i] = line.toMutableList()
                }
            }
            if(count < size) putRandomValues(1)
//            if(count == size)
                isGameOver = checkGameStatus()
        }
        fun moveUp() {
            var count = 0
            val field = inverseField(gameField)
            for (i in 0 until size){
                var line = clearZeroes(field[i])
                if (line.isNotEmpty()){
                    line = fillZeroes(merge(line, false), false)
                    if(field[i] == line.toMutableList()) count++
                    else field[i] = line.toMutableList()
                }
            }
            gameField = inverseField(field)
            if(count < size) putRandomValues(1)
//            if(count == size)
                isGameOver = checkGameStatus()
        }
        fun moveDown(){
            var count = 0
            val field = inverseField(gameField)
            for (i in 0 until size){
                var line = clearZeroes(field[i])
                if (line.isNotEmpty()){
                    line = fillZeroes(merge(line, true), true)
                    if(field[i] == line.toMutableList()) count++
                    else field[i] = line.toMutableList()
                }
            }
            gameField = inverseField(field)
            if(count < size) putRandomValues(1)
//            if(count == size)
                isGameOver = checkGameStatus()
        }
}