//fun grwid(size: Int){
//    repeat(size) {
//        repeat(size) {
//            print(" 0 ")
//        }
//        println("0")
//    }
//}
//fun oneLineFillWithZeros(size: Int, varargs: Int): List<Int> {
//    repeat(size) {
//a
//    }
//}
private var board = TestGrid(0)
fun main() {
    val size: Int = 10
    board = TestGrid(4)
    board.resetGameField()
    board.printGameField()
    while(!board.isGameOver){
        when(readLine()!!){
            "w" -> board.moveUp()
            "s" -> board.moveDown()
            "a" -> board.moveLeft()
            "d" -> board.moveRight()
        }
        board.printGameField()
    }
    println("proebal pohodu")

//    board.moveUp()
//    board.printGameField()
//    board.moveDown()
//    board.printGameField()
}