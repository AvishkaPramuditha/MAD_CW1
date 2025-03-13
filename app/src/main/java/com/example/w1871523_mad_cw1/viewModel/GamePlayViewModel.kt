package com.example.w1871523_mad_cw1.viewModel

import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.w1871523_mad_cw1.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    private var _buttonName= mutableStateOf("Throw")


    private var _availablePlayerReRoll: Int = 2
    private var _availableComputerReRoll: Int = 2
    private var _computerFirstThrow = true

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


    fun selectDice(index: Int) {
        _playerSelectedDices.value += index
    }

    fun removeSelectedDice(index: Int) {
        _playerSelectedDices.value -= index
    }

    fun setTarget(target: Int = 101) {
        _target.intValue = target
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
        _showSelection.value = _availablePlayerReRoll > 0

        if(_buttonName.value.split(" ")[0] == "Re-Roll"){_availablePlayerReRoll--}
        _buttonName.value= if(_availablePlayerReRoll >0){"Re-Roll ( ${_availablePlayerReRoll} )"}else{"Throw"}
    }

    fun throwDices() {
        _showAnimation.value = true


        viewModelScope.launch {


            if (_buttonName.value.split(" ")[0] == "Re-Roll"){
                val playerThrowing = launch {
                    delay(900)// waiting fo animation to finish
                    _showAnimation.value = false
                    throwPlayerDices()
                }

                playerThrowing.join()

            }else{

               val computerThrowing = launch {
                    delay(900)// waiting fo animation to finish
                    _showAnimation.value = false
                    throwComputerDices()
                }

                val playerThrowing = launch {
                    delay(900)// waiting fo animation to finish
                    _showAnimation.value = false
                    throwPlayerDices()
                }


                computerThrowing.join()
                playerThrowing.join()

            }




        }



//        viewModelScope.launch {
//            delay(900)// waiting fo animation to finish
//            _showAnimation.value = false
//            val result = viewModelScope.async {
//                throwComputerDices()
//            }
//            result.await()
//
//
//        }

//             viewModelScope.launch {
//            delay(900)// waiting fo animation to finish
//            _showAnimation.value = false
//
//            val result = viewModelScope.async {
//
//                throwPlayerDices()
//            }
//        }
    }


    // throw computer dices
    // randomly select whether to re-roll and which dices to re-roll
    private suspend fun throwComputerDices() {

        // throw dice
        val generateRandomDiceNumbers = generateRandomDiceNumbers()
        _computerDiceValues.value = generateRandomDiceNumbers


        //simulate thinking time
        delay(1500)

        //decide whether re-roll or not
        var reRoll = Random.nextBoolean()


        while (reRoll) {

            //if computer re-rolls
            if (_availableComputerReRoll > 0) {

                // select maximum 4 dices to keep. selecting all the dices to keep is not meaning full for computer player to re roll
                val randomDiceIndexes: List<Int> =
                    _computerDiceValues.value.indices.shuffled().take(Random.nextInt(1, 5))

                reRollSelectedDice(_computerDiceValues, randomDiceIndexes)
                _availableComputerReRoll--

                reRoll = Random.nextBoolean() // check whether re-roll again or not

                //simulate thinking time
                delay(1500)

            }else{
                break
            }

        }

        println("computer done")
    }

    fun scoreTotal() {
        _showSelection.value = false
        _showDice.value = false
        _playerSelectedDices.value = emptyList()

        _playerScore.intValue = calculateScore(_playerDiceValues.value, _playerScore.intValue)
        _computerScore.intValue = calculateScore(_computerDiceValues.value, _computerScore.intValue)

    }

    private fun generateRandomDiceNumbers(): MutableList<Int> {
        val dices: MutableList<Int> = mutableListOf()
        for (i in 1..5) {
            dices += Random.nextInt(1, 7)
        }
        return dices
    }

    private fun calculateScore(diceValues: List<Int>, currentScore: Int): Int {
        var total: Int = 0

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


}