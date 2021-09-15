class Grid(private val size: Int) {
    var isGameOver: Boolean = false
    private var moveCount: Int = 0
    private var score: Int = 0
    private var bestScore: Int = 0
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

    private fun clearZeroes(columns: MutableList<Int>) = columns.filter { cell: Int -> cell > 0 }

    private fun merge(list: List<Int>, n: IntProgression): List<Int> {
        val mergedList = list.toMutableList()
        if (list.size > 1) {
            for (i in n) {
                if (mergedList[i] == mergedList[i - 1]) {
                    mergedList[i] = 0
                    mergedList[i - 1] *= 2
                    //TODO надо бы score сюда запилить
                    //Здесь и происходит объединение ячеек
                }
            }
        }
        return mergedList.filter { cell: Int -> cell > 0 }
    }

    private fun checkGameStatus() {
        val list = gameField
        val list2 = MutableList(size) { MutableList(size) { 0 } }
        var count = 0
        for (i in 0 until size) {
            for (j in 0 until size) {
                list2[i][j] = gameField[j][i]
            }
            if (list[i].size == merge(list[i], list[i].indices).size) count++
            if (list2[i].size == merge(list[i], list2[i].indices).size) count++
        }
        if (count == 8) isGameOver = true
//        var count = 0
//        gameField.forEach{row -> row.forEach{element -> if(element != 0) count++}
//        }
//        if (count == size * size) isGameOver = true
    }

    fun resetGameField() {
        gameField.forEach { row -> for (i in row.indices) row[i] = 0 }
        putRandomValues(2)
        isGameOver = false
    }

    fun printGameField() {
        gameField.forEach { row ->
            row.forEach { element -> print(" $element ") }
            println()
        }
        println()
    }

    fun moveLeft() {
        var count = 0
        for (i in 0 until size) {
            if (clearZeroes(gameField[i]).isNotEmpty()) {
                val n: IntProgression = 1 until clearZeroes(gameField[i]).size
                val list = merge(clearZeroes(gameField[i]), n)
                if (list == gameField[i]) count++
                for (j in gameField[i].indices) gameField[i][j] = 0
                for (j in list.indices) gameField[i][j] = list[j]
            }
        }
        if (count < 4) putRandomValues(1)
        checkGameStatus()
    }

    fun moveRight() {
        var count = 0
        for (i in 0 until size) {
//            val list = clearZeros((gameField[i]))
            val n: IntProgression = clearZeroes(gameField[i]).size - 1 downTo 1
            val list = merge(clearZeroes((gameField[i])), n)
            if (list.size == size) count++
            val sizeDifference = size - list.size //Поправка на то, что слева направо
            for (j in gameField[i].indices) gameField[i][j] = 0
            for (j in list.indices) gameField[i][j + sizeDifference] = list[j]
        }
        if (count < 4) putRandomValues(1)
        checkGameStatus()
    }

    fun moveUp() {
        var count = 0
        for (i in 0 until size) {
            val list: MutableList<Int> = mutableListOf()
            for (j in 0 until size) {
                list.add(gameField[j][i])
            }
            println(clearZeroes(list).isNotEmpty())
            if (clearZeroes(list).isNotEmpty()) {
                val n: IntProgression = 1 until clearZeroes(list).size
                println(n)
                val list2 = merge(clearZeroes(list), n)
                if (list2.size == size) count++
                for (j in 0 until size) gameField[j][i] = 0
                for (j in list2.indices) gameField[j][i] = list2[j]
            }
        }
        if (count < 4) putRandomValues(1)
        checkGameStatus()
    }

    fun moveDown() {
        var count = 0
        for (i in 0 until size) {
            val list: MutableList<Int> = mutableListOf()
            for (j in 0 until size) {
                list.add(gameField[j][i])
            }
            val n: IntProgression = clearZeroes(list).size - 1 downTo 1
            val list2 = merge(clearZeroes(list), n)
            if (list2.size == size) count++
            val sizeDifference = size - list2.size
            for (j in 0 until size) gameField[j][i] = 0
            for (j in list2.indices) gameField[j + sizeDifference][i] = list2[j]
        }
        if (count < 4) putRandomValues(1)
        checkGameStatus()
    }
}