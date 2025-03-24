package com.example.w1871523_mad_cw1

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class GamePlayActivity : ComponentActivity() {

    private val targetState = mutableStateOf("101")

    private var playerDiceValues = mutableStateOf(listOf(1, 1, 1, 1, 1))
    private var computerDiceValues = mutableStateOf(listOf(1, 1, 1, 1, 1))
    private var playerSelectedDices = mutableStateOf<List<Int>>(emptyList())
    private var target = mutableIntStateOf(101)
    private var playerScore = mutableIntStateOf(0)
    private var computerScore = mutableIntStateOf(0)
    private var playerWon = mutableIntStateOf(0)
    private var computerWon = mutableIntStateOf(0)
    private var showSelection = mutableStateOf(false)
    private var showDice = mutableStateOf(false)
    private var showAnimation = mutableStateOf(false)
    private var buttonName = mutableStateOf("Throw")
    private var showWinner = mutableStateOf(false)
    private var showTargetAlert = mutableStateOf(true)
    private var showScoreButton = mutableStateOf(false)
    private var showThrowButton = mutableStateOf(true)
    private var isGameFinished = mutableStateOf(false)

    private var availablePlayerReRoll: Int = 2
    private var availableComputerReRoll: Int = 2
    private var reRollCounter = 0
    private lateinit var gameLevel:String

    private var rollingSoundPlayer: MediaPlayer? = null
    private var winSoundPlayer: MediaPlayer? = null
    private var loseSoundPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        playerWon.intValue=MainActivity.humanPlayerScore
        computerWon.intValue=MainActivity.computerPlayerScore
        gameLevel=intent.getStringExtra("gameLevel") ?: "Easy"

        setContent {
            val isPortrait =
                LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
                MainView(isPortrait)
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("targetState", targetState.value)

        outState.putIntArray("playerDiceValues", playerDiceValues.value.toIntArray())
        outState.putIntArray("computerDiceValues", computerDiceValues.value.toIntArray())
        outState.putIntArray("playerSelectedDices", playerSelectedDices.value.toIntArray())
        outState.putInt("target", target.intValue)
        outState.putInt("playerScore", playerScore.intValue)
        outState.putInt("computerScore", computerScore.intValue)
        outState.putInt("playerWon", playerWon.intValue)
        outState.putInt("computerWon", computerWon.intValue)
        outState.putBoolean("showSelection", showSelection.value)
        outState.putBoolean("showDice", showDice.value)
        outState.putBoolean("showAnimation", showAnimation.value)
        outState.putString("buttonName", buttonName.value)
        outState.putBoolean("showWinner", showWinner.value)
        outState.putBoolean("showTargetAlert", showTargetAlert.value)
        outState.putBoolean("showScoreButton", showScoreButton.value)
        outState.putBoolean("showThrowButton", showThrowButton.value)
        outState.putBoolean("isGameFinished", isGameFinished.value)

        outState.putInt("availablePlayerReRoll", availablePlayerReRoll)
        outState.putInt("availableComputerReRoll", availableComputerReRoll)
        outState.putInt("reRollCounter", reRollCounter)
        outState.putString("gameLevel",gameLevel)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        targetState.value = savedInstanceState.getString("targetState") ?: "101"

        playerDiceValues.value= savedInstanceState.getIntArray("playerDiceValues")?.toList() ?: listOf(1, 1, 1, 1, 1)
        computerDiceValues.value=savedInstanceState.getIntArray("computerDiceValues")?.toList() ?: listOf(1, 1, 1, 1, 1)
        playerSelectedDices.value=savedInstanceState.getIntArray("playerSelectedDices")?.toList() ?: listOf(1, 1, 1, 1, 1)
        target.intValue=savedInstanceState.getInt("target")
        playerScore.intValue=savedInstanceState.getInt("playerScore")
        computerScore.intValue=savedInstanceState.getInt("computerScore")
        playerWon.intValue=savedInstanceState.getInt("playerWon")
        computerWon.intValue=savedInstanceState.getInt("computerWon")
        showSelection.value=savedInstanceState.getBoolean("showSelection")
        showDice.value=savedInstanceState.getBoolean("showDice")
        showAnimation.value=savedInstanceState.getBoolean("showAnimation")
        buttonName.value=savedInstanceState.getString("buttonName") ?:"Throw"
        showWinner.value=savedInstanceState.getBoolean("showWinner")
        showTargetAlert.value=savedInstanceState.getBoolean("showTargetAlert")
        showScoreButton.value=savedInstanceState.getBoolean("showScoreButton")
        showThrowButton.value=savedInstanceState.getBoolean("showThrowButton")
        isGameFinished.value=savedInstanceState.getBoolean("isGameFinished")

        availablePlayerReRoll=savedInstanceState.getInt("availablePlayerReRoll")
        availableComputerReRoll=savedInstanceState.getInt("availableComputerReRoll")
        reRollCounter=savedInstanceState.getInt("reRollCounter")
        gameLevel=savedInstanceState.getString("gameLevel") ?: "Easy"

    }


    override fun onDestroy() {
        super.onDestroy()
        rollingSoundPlayer?.release()
        winSoundPlayer?.release()
        loseSoundPlayer?.release()
        println("destroy")
    }

    //********* composable functions ********************


    @Composable
    fun MainView(isPortrait: Boolean) {
        SetTarget()
        WinnerAlert()

        Box(
            modifier = if (showWinner.value) {
                Modifier.blur(7.dp)
            } else {
                Modifier
            }
        ) {
            Image(
                painter = painterResource(id = R.drawable.play_background),
                contentDescription = "background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // get screen with and set dynamic padding
            val screenWith = LocalConfiguration.current.screenWidthDp.dp
            val padding = when {
                screenWith < 320.dp -> 3.dp
                screenWith < 380.dp -> 5.dp
                screenWith < 400.dp -> 10.dp
                else -> 15.dp
            }

            Column(
                modifier = Modifier
                    .padding(
                        top = if (isPortrait) {
                            60.dp
                        } else {
                            25.dp
                        }, end = padding, start = padding, bottom = 5.dp
                    )
                    .fillMaxSize()
            ) {
                if (isPortrait) {
                    ScoreBoard()
                    Spacer(modifier = Modifier.height(30.dp))
                    RollingBoard(true)
                    Spacer(modifier = Modifier.height(30.dp))
                    ButtonSection(true)
                } else {
                    ScoreBoardLandscape()
                    Spacer(modifier = Modifier.height(10.dp))
                    RollingBoard(false)
                    Spacer(modifier = Modifier.height(10.dp))
                    ButtonSection(false)
                }

            }

            if (isGameFinished.value) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .clickable(enabled = false) {})
            }

        }

    }

    @Composable
    fun ScoreBoard() {
        Card(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp), elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.scoreboard_background),
                    contentDescription = "Scoreboard Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxSize()
                ) {
                    Card(
                        modifier = Modifier
                            .height(150.dp)
                            .width(205.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 15.dp, start = 15.dp, bottom = 10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp)
                            ) {
                                Text(
                                    "Leader Board",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(top = 5.dp)

                                )
                                Spacer(modifier = Modifier.width(22.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.fire),
                                    contentDescription = "Scoreboard Background",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .align(Alignment.CenterVertically)
                                )

                            }

                            Text(
                                "H  : ${playerWon.intValue}",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.width(180.dp)
                            )
                            Text(
                                "C   : ${computerWon.intValue}",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .width(180.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .height(180.dp)
                            .width(120.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(end = 10.dp, start = 5.dp, top = 10.dp, bottom = 6.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Human",
                                        modifier = Modifier.size(34.dp),
                                        tint = Color(0xFF01497C)
                                    )
                                    Text(
                                        ": ${playerScore.intValue}", fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold,

                                        )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.monitor_icon),
                                        contentDescription = "Computer",
                                        colorFilter = ColorFilter.tint(Color(0xFF01497C)),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(39.dp)
                                    )
                                    Text(
                                        ": ${computerScore.intValue}", fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 5.dp)
                                    )
                                }
                            }
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFDADA))
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 14.dp, top = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.target_icon),
                                    contentDescription = "target",
                                    tint = Color.Red,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "${target.intValue}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                )

                            }
                        }
                    }
                }


            }
        }
    }

    @Composable
    fun RollingBoard(isPortrait: Boolean) {

        val diceImages = listOf(
            R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6
        )

        Card(
            modifier = Modifier
                .height(
                    if (isPortrait) {
                        460.dp
                    } else {
                        250.dp
                    }
                )
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.rolling_background),
                    contentScale = ContentScale.Crop,
                    contentDescription = "rolling background",
                    modifier = Modifier.fillMaxWidth()
                )

                if (isPortrait) {
                    Rolling(diceImages, true)
                } else {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = 5.dp,
                                bottom = 5.dp,
                                start = 15.dp,
                                end = 15.dp
                            )
                    ) {
                        Rolling(diceImages, false)

                        if (showAnimation.value) {
                            RollingAnimation()
                            Spacer(modifier = Modifier.width(30.dp))
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun Rolling(diceImages: List<Int>, isPortrait: Boolean) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = if (isPortrait) {
                Modifier
                    .fillMaxSize()
                    .padding(
                        top = 10.dp,
                        bottom = 10.dp,
                        start = 15.dp,
                        end = 15.dp
                    )
            } else {
                Modifier
                    .width(500.dp)
                    .fillMaxHeight()
            }
        ) {
            ///---------------
            Text(
                "The Opponent",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            DiceSet(diceImages, false, isPortrait)

            if (isPortrait) {
                Spacer(modifier = Modifier.height(10.dp))//*****************

                if (showAnimation.value) {
                    RollingAnimation()
                } else {
                    Spacer(modifier = Modifier.size(220.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))//*********************
            } else {
                Spacer(modifier = Modifier.height(50.dp))
            }


            Text(
                if (showSelection.value) "Select Dice To Keep" else "",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFFEEEDEB)
            )

            Spacer(modifier = Modifier.height(5.dp))
            DiceSet(diceImages, true, isPortrait)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "You",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            ///----------------
        }
    }

    @Composable
    fun DiceSet(
        diceImages: List<Int>,
        user: Boolean,
        isPortrait: Boolean
    ) {
        val diceValues =
            if (user) playerDiceValues.value else computerDiceValues.value

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            for ((index, dice) in diceValues.withIndex()) {
                Box(
                    modifier = Modifier
                        .size(
                            if (isPortrait) {
                                55.dp
                            } else {
                                50.dp
                            }
                        )
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            width = if (user && index in playerSelectedDices.value) 4.dp else 0.dp,
                            color = if (user && index in playerSelectedDices.value) Color.Green else Color.Transparent,
                            shape = RoundedCornerShape(15.dp),
                        )
                        .clickable(enabled = user && showSelection.value) {
                            if (index in playerSelectedDices.value) {
                                playerSelectedDices.value -= index
                            } else {
                                playerSelectedDices.value += index
                            }
                        }
                ) {
                    if (showDice.value) {
                        Image(
                            painter = painterResource(id = diceImages[dice - 1]),
                            contentScale = ContentScale.Crop,
                            contentDescription = "DICE1",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                }
            }

        }
    }


    @Composable
    fun ButtonSection(isPortrait: Boolean) {

        val context = LocalContext.current //check
        val rememberCoroutineScope = rememberCoroutineScope()
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isPortrait) {
                Arrangement.SpaceBetween
            } else {
                Arrangement.SpaceAround
            }
        ) {

            ElevatedButton(
                onClick = {
                    rememberCoroutineScope.launch {
                        playRollingSound(context) //check
                        throwDices()
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF067EBF)
                ),
                modifier = Modifier
                    .height(50.dp)
                    .width(180.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
                shape = RoundedCornerShape(20.dp),
                enabled = showThrowButton.value

            ) {

                Text(
                    text = buttonName.value,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            }
            ElevatedButton(
                onClick = {
                    rememberCoroutineScope.launch {
                        scoreTotal()
                    }

                }, colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF067EBF)

                ),
                modifier = Modifier
                    .height(50.dp)
                    .width(150.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
                shape = RoundedCornerShape(20.dp),
                enabled = showScoreButton.value && playerSelectedDices.value.isEmpty()
            ) {

                Text(
                    "Score",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }

    @Composable
    fun SetTarget() {
        var targetText by remember { targetState }
        var validatedTargetInput: Boolean

        if (showTargetAlert.value) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Set Target", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.target_icon),
                            contentDescription = "target",
                            tint = Color.Red,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },

                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = targetText,
                            onValueChange = {
                                validatedTargetInput = validateTargetInput(it)
                                if (validatedTargetInput) {
                                    targetText = it
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(51.dp)
                                .shadow(5.dp, RoundedCornerShape(20.dp)),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                unfocusedTextColor = Color.Black,
                                focusedIndicatorColor = Color(0xFF067EBF),
                                unfocusedIndicatorColor = Color(0xFF067EBF),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    }
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ElevatedButton(
                            onClick = {
                                validatedTargetInput = validateTargetInput(targetText)
                                if (validatedTargetInput) {
                                    target.intValue = targetText.toInt()
                                    showTargetAlert.value = false
                                    targetText = "101"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color(0xFF067EBF)
                            ),
                            modifier = Modifier
                                .height(40.dp)
                                .width(100.dp),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp),

                            ) {

                            Text(
                                "Done",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }
            )
        }


    }

    @SuppressLint("NewApi")
    @Composable
    fun RollingAnimation() {

        val context = LocalContext.current
        var animatedDrawable by remember { mutableStateOf<AnimatedImageDrawable?>(null) }

        LaunchedEffect(Unit) {
            val drawable: Drawable? =
                ResourcesCompat.getDrawable(context.resources, R.drawable.rolling_animation, null)
            if (drawable is AnimatedImageDrawable) {
                animatedDrawable = drawable
                drawable.start()
            }
        }

        Canvas(
            modifier = Modifier
                .size(220.dp)
                .padding(start = 30.dp, top = 35.dp)
        ) {
            animatedDrawable?.draw(drawContext.canvas.nativeCanvas)
        }
    }

    @Composable
    fun WinnerAlert() {

        val images = listOf(
            R.drawable.you_win,
            R.drawable.you_lose
        )

        val playerWin = playerScore.intValue > computerScore.intValue
        val context = LocalContext.current

        if (showWinner.value) {
            AlertDialog(
                onDismissRequest = {
                    showWinner.value = false
                },
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (playerWin) {
                                    images[0]
                                } else {
                                    images[1]
                                }
                            ),
                            contentDescription = if (playerWin) {
                                "win"
                            } else {
                                "lose"
                            },
                        )
                    }
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "  Your Score : ${playerScore.intValue}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold, modifier = Modifier.width(180.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "  Bot Score   : ${computerScore.intValue}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold, modifier = Modifier.width(180.dp)
                        )
                    }

                },
                confirmButton = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ElevatedButton(
                            onClick = {
                                showWinner.value = false
                            },
                            modifier = Modifier
                                .height(40.dp)
                                .width(100.dp),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                if (playerWin) {
                                    Color(0xFF41B06E)
                                } else {
                                    Color(0xFFE52020)
                                }
                            )

                        ) {
                            Text(
                                "Done",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                },
                containerColor = Color.White,
            )
            if (playerWin) {
                playWinSound(context) // check
            } else {
                playLoseSound(context) // check
            }
        }

    }


    // Landscape Composable Components
    @Composable
    fun ScoreBoardLandscape() {
        Card(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp), elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.scoreboard_background),
                    contentDescription = "Scoreboard Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 5.dp, start = 5.dp, end = 5.dp)
                        .fillMaxSize()
                ) {
                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .width(305.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 6.dp, top = 6.dp, end = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween

                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.fire),
                                contentDescription = "Scoreboard Background",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(25.dp)

                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "You  : ${playerWon.intValue}",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 23.sp,
                                modifier = Modifier.width(120.dp)
                            )

                            Text(
                                " /  ",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 23.sp,
                                modifier = Modifier.width(15.dp)
                            )
                            Text(
                                "Bot   : ${computerWon.intValue}",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 23.sp,
                                modifier = Modifier.width(121.dp)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .width(95.dp)
                            .height(40.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFDADA))
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 8.dp, top = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.target_icon),
                                contentDescription = "target",
                                tint = Color.Red,
                                modifier = Modifier.size(25.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                "${target.intValue}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 23.sp
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .width(235.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 5.dp, end = 5.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .width(105.dp)
                                    .fillMaxHeight()

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Human",
                                    modifier = Modifier.size(30.dp),
                                    tint = Color(0xFF01497C)
                                )
                                Text(
                                    ": ${playerScore.intValue}", fontSize = 23.sp,
                                    fontWeight = FontWeight.Bold,

                                    )
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .width(110.dp)
                                    .fillMaxHeight()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.monitor_icon),
                                    contentDescription = "Computer",
                                    colorFilter = ColorFilter.tint(Color(0xFF01497C)),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(38.dp)
                                )
                                Text(
                                    ": ${computerScore.intValue}", fontSize = 23.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }
                    }


                }


            }
        }
    }


    //*****************  business logic functions *****************

    private fun throwPlayerDices() {
        showDice.value = true

        if (availablePlayerReRoll > 0 && playerSelectedDices.value.isNotEmpty()) {
            reRollSelectedDice(playerDiceValues, playerSelectedDices.value)
        } else {
            val generateRandomDiceNumbers = generateRandomDiceNumbers()
            playerDiceValues.value = generateRandomDiceNumbers
        }

        playerSelectedDices.value = emptyList()
        showScoreButton.value = false

        if (buttonName.value.split(" ")[0] == "Re-Roll") {
            availablePlayerReRoll--
        }
        if (availablePlayerReRoll > 0) {
            buttonName.value = "Re-Roll ( $availablePlayerReRoll )"
        } else {
            buttonName.value = "Throw"
            showThrowButton.value = false
        }
        showSelection.value = availablePlayerReRoll > 0
    }


    private suspend fun throwDices() {

        showAnimation.value = true
        showScoreButton.value = false

            if (buttonName.value.split(" ")[0] == "Re-Roll") {
                reRollCounter++
                delay(900)// waiting fo animation to finish
                showAnimation.value = false
                throwPlayerDices()

            } else {
                showDice.value=false// to conform
                reRollCounter = 0
                delay(900)// waiting fo animation to finish
                showAnimation.value = false
                throwComputerDices()
                throwPlayerDices()
            }
            if (availablePlayerReRoll == 0 && reRollCounter == 2) {
                //delay(500)
                scoreTotal()
                availablePlayerReRoll--
                showScoreButton.value = false
            }else{
                showScoreButton.value = true
            }

    }


    private  fun throwComputerDices() {
        val generateRandomDiceNumbers = generateRandomDiceNumbers()
        computerDiceValues.value = generateRandomDiceNumbers
    }

    private suspend fun scoreTotal() {
        showSelection.value = false
        //showDice.value = false
        playerSelectedDices.value = emptyList()
        showScoreButton.value = false



        if (gameLevel == "Hard"){
            computerHardLevelReRolling()
        }else{
            computerEasyLevelReRolling()
        }

        showThrowButton.value = true

        val calculatedPlayerScore = calculateScore(playerDiceValues.value, playerScore.intValue)
        val calculatedComputerScore =
            calculateScore(computerDiceValues.value, computerScore.intValue)

        computerScore.intValue = calculatedComputerScore
        playerScore.intValue = calculatedPlayerScore
        buttonName.value = "Throw"

        if (calculatedPlayerScore >= target.intValue &&
            calculatedComputerScore >= target.intValue &&
            calculatedPlayerScore == calculatedComputerScore
        ) {
            availablePlayerReRoll = -1
            availableComputerReRoll = -1

        } else if (calculatedComputerScore >= target.intValue && calculatedPlayerScore < calculatedComputerScore) {
            showWinner.value = true
            isGameFinished.value = true
            computerWon.intValue += 1
            MainActivity.setComputerPlayerScore(computerWon.intValue)


        } else if (calculatedPlayerScore >= target.intValue && calculatedComputerScore < calculatedPlayerScore) {
            showWinner.value = true
            isGameFinished.value = true
            playerWon.intValue += 1
            MainActivity.setHumanPlayerScore(playerWon.intValue)
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
        val mutableList = diceValues.value.toMutableList()

        for (index in diceValues.value.indices) {
            if (index !in selectedDices) {
                val newNumber = Random.nextInt(1, 7)
                mutableList[index] = newNumber
            }
        }

        diceValues.value = mutableList
    }

    private fun validateTargetInput(input: String): Boolean {
        return input.matches("^\\d+$".toRegex())
    }

    private fun playRollingSound(context: Context) {
        rollingSoundPlayer = MediaPlayer.create(context, R.raw.rolling_sound)
        rollingSoundPlayer?.start()
    }

    private fun playWinSound(context: Context) {
        winSoundPlayer = MediaPlayer.create(context, R.raw.win_sound)
        winSoundPlayer?.start()
    }

    private fun playLoseSound(context: Context) {
        loseSoundPlayer = MediaPlayer.create(context, R.raw.lose_sound)
        loseSoundPlayer?.start()
    }


    private suspend fun computerEasyLevelReRolling(){
        //decide whether re-roll or not
        var reRoll = Random.nextBoolean()

        while (reRoll) {
            if (availableComputerReRoll > 0) {


                // get random indexes to keep and others will re roll
                val randomDiceIndexes: List<Int> =
                    computerDiceValues.value.indices.shuffled().take(Random.nextInt(1, 6))

                // set rerolled dice set to show
                reRollSelectedDice(computerDiceValues, randomDiceIndexes)
                availableComputerReRoll--
                reRoll = Random.nextBoolean() // check whether re-roll again or not

                delay(2000)
            } else {
                break
            }

        }
    }

    private suspend fun computerHardLevelReRolling(){
        while (true) {

            val diceValues: MutableList<Int> = computerDiceValues.value.toList().toMutableList()
            val currentTotal = calculateScore(computerDiceValues.value, 0)
            val oneCount = diceValues.count { it == 1 }
            val twoCount = diceValues.count { it == 2 }

            if (availableComputerReRoll > 0 && currentTotal < 15) {
                for ((index, value) in diceValues.withIndex()) {
                    if (value < 4) {
                        diceValues[index] = Random.nextInt(1, 7)
                    }
                }
                availableComputerReRoll--

            } else if (availableComputerReRoll > 0 && currentTotal in 15..20) {
                if (oneCount >= 2 || twoCount >= 2) {
                    for ((index, value) in diceValues.withIndex()) {
                        if (value < 3) {
                            diceValues[index] = Random.nextInt(1, 7)
                        }
                    }
                    availableComputerReRoll--

                }else{

                    break
                }

            } else {

                break
            }

            computerDiceValues.value = diceValues
            delay(2000)
        }
    }

}





