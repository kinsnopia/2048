fun main() {

    val size = 4
    val board = Grid(size)

    board.resetGameField()
    println("Score: ${board.score} \n")

    board.printGameField()
    while (!board.isGameOver) {
        when (readLine()!!) {
            "w" -> board.move("w")
            "a" -> board.move("a")
            "s" -> board.move("s")
            "d" -> board.move("d")
        }
        println("Score: ${board.score} \n")
        board.printGameField()
    }
    if (board.isGameWon) println("krasava")
    else println("No available moves")

}