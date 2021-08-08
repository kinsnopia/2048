fun main() {
    val size = 4
    val board = TestGrid(size)

    board.resetGameField()
    println("Tvoy schyot: ${board.showScore()} \n")
    board.printGameField()
    while(!board.isGameOver){
        when(readLine()!!){
            "w" -> board.move("w")
            "s" -> board.move("s")
            "a" -> board.move("a")
            "d" -> board.move("d")
        }
        println("Tvoy schyot: ${board.showScore()} \n")
        board.printGameField()
    }
    if(board.isGameWon) println("krasava")
    else println("proebal pohodu")

}