package com.example.w1871523_mad_cw1

import android.annotation.SuppressLint
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.w1871523_mad_cw1.viewModel.GamePlayViewModel
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat


private val targetState= mutableStateOf("101")

class GamePlayActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val viewModel: GamePlayViewModel = viewModel()
            val isPortrait =
                LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
            MainView(viewModel, isPortrait)
        }

    }

     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("target",targetState.value)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        targetState.value=savedInstanceState.getString("target") ?: "101"
    }


}

@Composable
fun MainView(viewModel: GamePlayViewModel, isPortrait: Boolean) {

    SetTarget(viewModel)
    WinnerAlert(viewModel)

    Box(
        modifier = if (viewModel.showWinner.value) {
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
                ScoreBoard(viewModel)
                Spacer(modifier = Modifier.height(30.dp))
                RollingBoard(viewModel, true)
                Spacer(modifier = Modifier.height(30.dp))
                ButtonSection(viewModel, true)
            } else {
                ScoreBoardLandscape(viewModel)
                Spacer(modifier = Modifier.height(10.dp))
                RollingBoard(viewModel, false)
                Spacer(modifier = Modifier.height(10.dp))
                ButtonSection(viewModel, false)
            }

        }

        if (viewModel.isGameFinished.value) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable(enabled = false) {})
        }

    }

}

@Composable
fun ScoreBoard(viewModel: GamePlayViewModel) {
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
                            "You  : ${viewModel.playerWon.value}",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.width(180.dp)
                        )
                        Text(
                            "Bot   : ${viewModel.computerWon.value}",
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
                                    ": ${viewModel.playerScore.value}", fontSize = 25.sp,
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
                                    ": ${viewModel.computerScore.value}", fontSize = 25.sp,
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
                                "${viewModel.target.value}",
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
fun RollingBoard(viewModel: GamePlayViewModel, isPortrait: Boolean) {

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
                Rolling(diceImages, viewModel, true)
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
                    Rolling(diceImages, viewModel, false)

                    if (viewModel.showAnimation.value) {
                        RollingAnimation()
                        Spacer(modifier = Modifier.width(30.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun Rolling(diceImages: List<Int>, viewModel: GamePlayViewModel, isPortrait: Boolean) {

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
        DiceSet(diceImages, viewModel, false, isPortrait)

        if (isPortrait) {
            Spacer(modifier = Modifier.height(10.dp))//*****************

            if (viewModel.showAnimation.value) {
                RollingAnimation()
            } else {
                Spacer(modifier = Modifier.size(220.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))//*********************
        } else {
            Spacer(modifier = Modifier.height(50.dp))
        }


        Text(
            if (viewModel.showSelection.value) "Select Dice To Keep" else "",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFFEEEDEB)
        )

        Spacer(modifier = Modifier.height(5.dp))
        DiceSet(diceImages, viewModel, true, isPortrait)
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
    viewModel: GamePlayViewModel,
    user: Boolean,
    isPortrait: Boolean
) {
    val diceValues =
        if (user) viewModel.playerDiceValues.value else viewModel.computerDiceValues.value

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
                        width = if (user && index in viewModel.playerSelectedDices.value) 4.dp else 0.dp,
                        color = if (user && index in viewModel.playerSelectedDices.value) Color.Green else Color.Transparent,
                        shape = RoundedCornerShape(15.dp),
                    )
                    .clickable(enabled = user && viewModel.showSelection.value) {
                        if (index in viewModel.playerSelectedDices.value) {
                            viewModel.removeSelectedDice(index)
                        } else {
                            viewModel.selectDice(index)
                        }
                    }
            ) {
                if (viewModel.showDice.value) {
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
fun ButtonSection(viewModel: GamePlayViewModel, isPortrait: Boolean) {

    val context = LocalContext.current
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
                viewModel.playRollingSound(context)
                viewModel.throwDices()
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
            enabled = viewModel.showThrowButton.value

        ) {

            Text(
                text = viewModel.buttonName.value,
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
        }
        ElevatedButton(
            onClick = {
                viewModel.scoreTotal()
            }, colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF067EBF)
            ),
            modifier = Modifier
                .height(50.dp)
                .width(150.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(20.dp),
            enabled = viewModel.showScoreButton.value && viewModel.playerSelectedDices.value.isEmpty()
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
fun SetTarget(viewModel: GamePlayViewModel) {
    var target by remember { targetState }
    var validatedTargetInput:Boolean

    if (viewModel.showTargetAlert.value) {
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
                        value = target,
                        onValueChange = {
                            validatedTargetInput = viewModel.validateTargetInput(it)
                            if (validatedTargetInput) {
                                target = it
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
                            validatedTargetInput = viewModel.validateTargetInput(target)
                            if (validatedTargetInput) {
                                viewModel.setTarget(target.toInt())
                                viewModel.setShowTargetAlert(false)
                                target="101"
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
fun WinnerAlert(viewModel: GamePlayViewModel) {

    val images = listOf(
        R.drawable.you_win,
        R.drawable.you_lose
    )

    val playerWin = viewModel.playerScore.value > viewModel.computerScore.value
    val context = LocalContext.current

    if (viewModel.showWinner.value) {
        AlertDialog(
            onDismissRequest = {
                viewModel.setShowWinner(false)
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
                        "  Your Score : ${viewModel.playerScore.value}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold, modifier = Modifier.width(180.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "  Bot Score   : ${viewModel.computerScore.value}",
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
                            viewModel.setShowWinner(false)
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
            viewModel.playWinSound(context)
        } else {
            viewModel.playLoseSound(context)
        }
    }

}


// Landscape Composable Components
@Composable
fun ScoreBoardLandscape(viewModel: GamePlayViewModel) {
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
                            "You  : ${viewModel.playerWon.value}",
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
                            "Bot   : ${viewModel.computerWon.value}",
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
                            "${viewModel.target.value}",
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
                                ": ${viewModel.playerScore.value}", fontSize = 23.sp,
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
                                ": ${viewModel.computerScore.value}", fontSize = 23.sp,
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


