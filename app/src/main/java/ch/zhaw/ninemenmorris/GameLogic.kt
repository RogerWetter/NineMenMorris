package ch.zhaw.ninemenmorris

class GameLogic {
    enum class PointState {
        Free, Black, BlackMarked, White, WhiteMarked, Marked
    }

    enum class GameState {
        Set, Train
    }

    enum class Player {
        Black, White
    }

    private val possibleMorris: Array<IntArray> = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(9, 10, 11),
        intArrayOf(12, 13, 14),
        intArrayOf(15, 16, 17),
        intArrayOf(18, 19, 20),
        intArrayOf(21, 22, 23),
        intArrayOf(0, 9, 21),
        intArrayOf(3, 10, 18),
        intArrayOf(6, 11, 15),
        intArrayOf(1, 4, 7),
        intArrayOf(16, 19, 22),
        intArrayOf(8, 12, 17),
        intArrayOf(5, 13, 20),
        intArrayOf(2, 14, 23)
    )

    private val possibleTrain: Array<IntArray> = arrayOf(
        intArrayOf(1, 9),
        intArrayOf(0, 4, 2),
        intArrayOf(1, 14),
        intArrayOf(4, 10),
        intArrayOf(1, 3, 5, 7),
        intArrayOf(4, 13),
        intArrayOf(7, 11),
        intArrayOf(4, 6, 8),
        intArrayOf(7, 12),
        intArrayOf(0, 10, 21),
        intArrayOf(3, 9, 11, 18),
        intArrayOf(6, 10, 15),
        intArrayOf(8, 13, 17),
        intArrayOf(5, 12, 14, 20),
        intArrayOf(2, 13, 23),
        intArrayOf(11, 16),
        intArrayOf(15, 17, 19),
        intArrayOf(12, 16),
        intArrayOf(10, 19),
        intArrayOf(16, 18, 20, 22),
        intArrayOf(13, 19),
        intArrayOf(9, 22),
        intArrayOf(19, 21, 23),
        intArrayOf(14, 22),
    )

    private var pickupState: Boolean = false
    private var isTraining: Boolean = false
    private var activeGameState: GameState = GameState.Set
    private var markedPoint: Int? = null
    private var activePlayer: Player = Player.White
    private val field = Array(24) { PointState.Free }
    private var whiteCoinsStock: Int = 9
    private var blackCoinsStock: Int = 9
    private var whiteCoinsActive: Int = 0
    private var blackCoinsActive: Int = 0
    private var win: Boolean = false

    fun pointClicked(pointNumber: Int): Array<PointState> {
        if (win) return field
        if (pointNumber < 0 || pointNumber >= 24) error("impossible pointNumber: $pointNumber")
        if (pickupState) handlePickUp(pointNumber)
        else {
            when (activeGameState) {
                GameState.Set -> handleSet(pointNumber)
                GameState.Train -> handleTrain(pointNumber)
            }
        }
        if (activeGameState === GameState.Train && !isTraining && !canPlayerMove()) {
            win = true
            changePlayer()
        }
        if (win) unmarkEverything()
        return field
    }

    fun getWin(): Boolean {
        return win
    }

    fun getActivePlayer(): Player {
        return activePlayer
    }

    fun getWhiteCoinsStock(): Int {
        return whiteCoinsStock
    }

    fun getBlackCoinsStock(): Int {
        return blackCoinsStock
    }

    private fun handleSet(pointNumber: Int) {
        when (field[pointNumber]) {
            PointState.Free -> setCoin(pointNumber)
            PointState.Black, PointState.BlackMarked, PointState.White, PointState.WhiteMarked, PointState.Marked -> {/*Do nothing*/
            }
        }
    }

    private fun setCoin(pointNumber: Int) {
        field[pointNumber] = getPointStateFromActivePlayer()
        if (activePlayer == Player.White) {
            whiteCoinsStock--
            whiteCoinsActive++
        } else {
            blackCoinsStock--
            blackCoinsActive++
        }
        if (whiteCoinsStock == 0 && blackCoinsStock == 0) activeGameState = GameState.Train
        checkOrChange(pointNumber)
    }

    private fun checkOrChange(pointNumber: Int) {
        if (checkAllMorris(pointNumber)) {
            if (activeGameState === GameState.Train &&
                ((whiteCoinsActive == 3 && activePlayer === Player.Black)
                        || (blackCoinsActive == 3 && activePlayer === Player.White))
            ) {
                win = true
            }
            pickupState = true
            markCoins()
        } else {
            changePlayer()
        }
    }

    private fun handleTrain(pointNumber: Int) {
        when (field[pointNumber]) {
            PointState.Free -> unmark()
            PointState.BlackMarked, PointState.WhiteMarked -> return
            PointState.Black -> {
                handleMark(Player.Black, pointNumber)
            }
            PointState.White -> {
                handleMark(Player.White, pointNumber)
            }
            PointState.Marked -> {
                if (markedPoint === null) return
                field[markedPoint!!] = PointState.Free
                unmark()
                field[pointNumber] = getPointStateFromActivePlayer()
                checkOrChange(pointNumber)
            }
        }
    }

    private fun handleMark(player: Player, pointNumber: Int) {
        unmark()
        if (activePlayer !== player) return
        if ((if (activePlayer === Player.White) whiteCoinsActive else blackCoinsActive) == 3) {
            markAllFreePoints(pointNumber)
        } else {
            mark(pointNumber)
        }
        isTraining = true
    }

    private fun unmark() {
        for (i in field.indices) {
            if (field[i] === PointState.Marked) field[i] = PointState.Free
        }
        markedPoint = null
        isTraining = false
    }

    private fun unmarkEverything() {
        for (i in field.indices) {
            when (field[i]) {
                PointState.Free, PointState.White, PointState.Black -> {}
                PointState.Marked -> field[i] = PointState.Free
                PointState.WhiteMarked -> field[i] = PointState.White
                PointState.BlackMarked -> field[i] = PointState.Black
            }
        }
        markedPoint = null
        isTraining = false
    }

    private fun mark(pointNumber: Int) {
        if (markedPoint !== null) return
        markedPoint = pointNumber
        setPossibleTrainMarked(pointNumber)
    }

    private fun canPlayerMove(): Boolean {
        for (i in field.indices) {
            when (field[i]) {
                PointState.Black -> {
                    if (activePlayer === Player.Black) {
                        setPossibleTrainMarked(i)
                    }
                }
                PointState.White -> {
                    if (activePlayer == Player.White) {
                        setPossibleTrainMarked(i)
                    }
                }
                else -> {}
            }
        }
        for (i in field.indices) {
            when (field[i]) {
                PointState.Marked -> {
                    unmarkEverything()
                    return true
                }
                else -> {}
            }
        }
        return false
    }

    private fun markAllFreePoints(pointNumber: Int) {
        if (markedPoint !== null) return
        markedPoint = pointNumber
        for (i in field.indices) {
            if (field[i] === PointState.Free) field[i] = PointState.Marked
        }
    }

    private fun markCoins() {
        changePlayer()
        isTraining = true
        var counter = 0
        val coinsInMorris = mutableListOf<Int>()
        for (i in field.indices) {
            if (field[i] === getPointStateFromActivePlayer()) {
                if (checkAllMorris(i)) coinsInMorris.add(i)
                else {
                    counter++
                    field[i] =
                        if (activePlayer == Player.White) PointState.WhiteMarked
                        else PointState.BlackMarked
                }
            }
        }
        if (counter == 0) {
            for (i in coinsInMorris) {
                field[i] =
                    if (activePlayer == Player.White) PointState.WhiteMarked
                    else PointState.BlackMarked
            }
        }
        changePlayer()
        markedPoint = null
    }

    private fun unmarkCoins() {
        changePlayer()
        for (i in field.indices) {
            if (field[i] === if (activePlayer == Player.White) PointState.WhiteMarked
                else PointState.BlackMarked
            ) field[i] = getPointStateFromActivePlayer()
        }
        changePlayer()
        markedPoint = null
    }

    private fun setPossibleTrainMarked(pointNumber: Int) {
        possibleTrain[pointNumber].forEach { i: Int ->
            if (field[i] == PointState.Free) field[i] = PointState.Marked
        }
    }

    private fun handlePickUp(pointNumber: Int) {
        when (field[pointNumber]) {
            PointState.Free, PointState.Marked, PointState.Black, PointState.White -> return
            PointState.BlackMarked, PointState.WhiteMarked -> {
                if (getPointStateFromActivePlayer() === field[pointNumber]) return
                field[pointNumber] = PointState.Free
                if (activePlayer === Player.White) blackCoinsActive-- else whiteCoinsActive--
                unmarkCoins()
                pickupState = false
                changePlayer()
            }
        }
        isTraining = false
    }

    private fun getPointStateFromActivePlayer(): PointState {
        return if (activePlayer === Player.White) PointState.White else PointState.Black
    }

    private fun changePlayer() {
        activePlayer = if (activePlayer == Player.White) Player.Black else Player.White
    }

    private fun checkAllMorris(pointNumber: Int): Boolean {
        possibleMorris.forEach {
            if (it.contains(pointNumber) && checkMorris(
                    it[0], it[1], it[2],
                    getPointStateFromActivePlayer())
            ) {
                return true
            }
        }
        return false
    }

    private fun checkMorris(x: Int, y: Int, z: Int, pointState: PointState): Boolean {
        if (field[x] == pointState && field[y] == pointState && field[z] == pointState) return true
        return false
    }
}
