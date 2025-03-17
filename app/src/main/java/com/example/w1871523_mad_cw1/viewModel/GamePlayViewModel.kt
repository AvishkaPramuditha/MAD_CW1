package com.example.w1871523_mad_cw1.viewModel


import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.w1871523_mad_cw1.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GamePlayViewModel : ViewModel() {
    private var _playerDiceValues = mutableStateOf(listOf(1, 1, 1, 1, 1))
    private var _computerDiceValues = mutableStateOf(listOf(1, 1, 1, 1, 1))
    private var _playerSelectedDices = mutableStateOf<List<Int>>(emptyList())
    private var _target = mutableIntStateOf(101)
    private var _playerScore = mutableIntStateOf(0)
    private var _computerScore = mutableIntStateOf(0)
    private var _playerWon = mutableIntStateOf(0)
    private var _computerWon = mutableIntStateOf(0)
    private var _showSelection = mutableStateOf(false)
    private var _showDice = mutableStateOf(false)
    private var _showAnimation = mutableStateOf(false)
    private var _buttonName = mutableStateOf("Throw")
    private var _showWinner = mutableStateOf(false)
    private var _showScoreButton = mutableStateOf(false)
    private var _showThrowButton = mutableStateOf(true)
    private var _isGameFinished = mutableStateOf(false)


    private var _availablePlayerReRoll: Int = 2
    private var _availableComputerReRoll: Int = 2
    private var reRollCounter=0
    private val _rollingSound: Int = R.raw.rolling_sound


    val playerDiceValues: State<List<Int>> = _playerDiceValues
    val computerDiceValues: State<List<Int>> = _computerDiceValues
    val playerSelectedDices: State<List<Int>> = _playerSelectedDices
    val target: State<Int> = _target
    val playerScore: State<Int> = _playerScore
    val computerScore: State<Int> = _computerScore
    val playerWon: State<Int> = _playerWon
    val computerWon: State<Int> = _computerWon
    val showSelection: State<Boolean> = _showSelection
    val showDice: State<Boolean> = _showDice
    val showAnimation: State<Boolean> = _showAnimation
    val buttonName: State<String> = _buttonName
    val showWinner: State<Boolean> = _showWinner
    val showScoreButton: State<Boolean> = _showScoreButton
    val showThrowButton: State<Boolean> = _showThrowButton
    val isGameFinished: State<Boolean> = _isGameFinished


    fun selectDice(index: Int) {
        _playerSelectedDices.value += index
    }

    fun removeSelectedDice(index: Int) {
        _playerSelectedDices.value -= index
    }

    fun setTarget(target: Int = 101) {
        _target.intValue = target
    }

    fun setShowWinner(enable: Boolean) {
        _showWinner.value = enable
    }

    private fun throwPlayerDices() {
        _showDice.value = true

        if (_availablePlayerReRoll > 0 && _playerSelectedDices.value.isNotEmpty()) {
            reRollSelectedDice(_playerDiceValues, _playerSelectedDices.value)
        } else {
            val generateRandomDiceNumbers = generateRandomDiceNumbers()
            _playerDiceValues.value = generateRandomDiceNumbers
        }

        _playerSelectedDices.value = emptyList()
        _showScoreButton.value = false

        if (_buttonName.value.split(" ")[0] == "Re-Roll") {
            _availablePlayerReRoll--
        }
        if (_availablePlayerReRoll > 0) {
            _buttonName.value = "Re-Roll ( $_availablePlayerReRoll )"
        } else {
            _buttonName.value = "Throw"
            _showThrowButton.value = false
        }
        _showSelection.value = _availablePlayerReRoll > 0
    }



    fun throwDices() {
        _showAnimation.value = true
        _showScoreButton.value = false

        viewModelScope.launch {
            val playerThrowing: Job
            var computerThrowing: Job? = null

            if (_buttonName.value.split(" ")[0] == "Re-Roll") {
                reRollCounter++
                playerThrowing = launch {
                    delay(900)// waiting fo animation to finish
                    _showAnimation.value = false
                    throwPlayerDices()
                }

            } else {
                reRollCounter=0
                computerThrowing = launch {
                    delay(900)// waiting fo animation to finish
                    _showAnimation.value = false
                    throwComputerDices()
                }

                playerThrowing = launch {
                    delay(900)// waiting fo animation to finish
                    _showAnimation.value = false
                    throwPlayerDices()
                }

            }

            computerThrowing?.join()
            playerThrowing.join()
            _showScoreButton.value = true

            if (_availablePlayerReRoll == 0&&reRollCounter==2) {
                delay(500)
                scoreTotal()
                _availablePlayerReRoll--
            }
        }

    }


    // throw computer dices
    // randomly select whether to re-roll and which dices to re-roll
    private suspend fun throwComputerDices() {

        // throw dice
        val generateRandomDiceNumbers = generateRandomDiceNumbers()
        _computerDiceValues.value = generateRandomDiceNumbers

        //decide whether re-roll or not
        var reRoll = Random.nextBoolean()

        while (reRoll) {
            if (_availableComputerReRoll > 0) {

                //simulating processing time
                delay(1000)

                val randomDiceIndexes: List<Int> =
                    _computerDiceValues.value.indices.shuffled().take(Random.nextInt(1, 6))

                reRollSelectedDice(_computerDiceValues, randomDiceIndexes)

                _availableComputerReRoll--
                reRoll = Random.nextBoolean() // check whether re-roll again or not

            } else {
                break
            }

        }


    }

    fun scoreTotal() {
        _showSelection.value = false
        _showDice.value = false
        _playerSelectedDices.value = emptyList()
        _showScoreButton.value = false
        _showThrowButton.value = true

        val calculatedPlayerScore = calculateScore(_playerDiceValues.value, _playerScore.intValue)
        val calculatedComputerScore =
            calculateScore(_computerDiceValues.value, _computerScore.intValue)

        _computerScore.intValue = calculatedComputerScore
        _playerScore.intValue = calculatedPlayerScore
        _buttonName.value = "Throw"

        if (calculatedPlayerScore >= _target.intValue &&
            calculatedComputerScore >= _target.intValue &&
            calculatedPlayerScore == calculatedComputerScore
        ) {
            _availablePlayerReRoll = -1
            _availableComputerReRoll = -1

        } else if (calculatedComputerScore >= _target.intValue && calculatedPlayerScore < calculatedComputerScore) {
            _showWinner.value = true
            _isGameFinished.value = true
            _computerWon.value += 1


        } else if (calculatedPlayerScore >= _target.intValue && calculatedComputerScore < calculatedPlayerScore) {
            _showWinner.value = true
            _isGameFinished.value = true
            _playerWon.value += 1

        }
    }

    private fun generateRandomDiceNumbers(): MutableList<Int> {
        val dices: MutableList<Int> = mutableListOf()
        for (i in 1..5) {
            dices += Random.nextInt(1, 7)
        }
        return dices
    }

    private fun calculateScore(diceValues: List<Int>, currentScore: Int): Int {
        var total = 0

        for (value in diceValues) {
            total += value
        }
        return currentScore + total
    }

    //re-roll selected dices
    private fun reRollSelectedDice(
        diceValues: MutableState<List<Int>>,
        selectedDices: List<Int>
    ) {
        for (index in diceValues.value.indices) {
            if (index !in selectedDices) {
                val newNumber = Random.nextInt(1, 7)
                val mutableList = diceValues.value.toMutableList()
                mutableList[index] = newNumber
                diceValues.value = mutableList
            }

        }
    }

    fun playRollingSound(context: Context) {
        val rollingSound: MediaPlayer? = MediaPlayer.create(context, _rollingSound)
        rollingSound?.start()
    }

}