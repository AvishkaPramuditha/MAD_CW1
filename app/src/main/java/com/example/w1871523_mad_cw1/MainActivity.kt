package com.example.w1871523_mad_cw1

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties



class MainActivity : ComponentActivity() {

    private val showAlertDialog = mutableStateOf(false)
    private val showDLevel = mutableStateOf(false)
    private var gameLevel=mutableStateOf("Easy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val configuration = LocalConfiguration.current
            val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

            MainView(showAlertDialog, showDLevel,gameLevel,isPortrait)

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("showAlertDialog", showAlertDialog.value)
        outState.putBoolean("showDLevel", showDLevel.value)
        outState.putString("gameLevel",gameLevel.value)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        showAlertDialog.value = savedInstanceState.getBoolean("showAlertDialog")
        showDLevel.value = savedInstanceState.getBoolean("showDLevel")
        gameLevel.value=savedInstanceState.getString("gameLevel") ?: "Easy"
    }


    // store game win statics in memory
    companion object {
        private var _humanPlayerHasWon = 0
        private var _computerPlayerHasWon = 0

        val humanPlayerHasWon: Int get() = _humanPlayerHasWon
        val computerPlayerHasWon: Int get() = _computerPlayerHasWon


        fun setHumanPlayerScore(score: Int) {
            _humanPlayerHasWon = score
        }

        fun setComputerPlayerScore(score: Int) {
            _computerPlayerHasWon = score
        }
    }

}

@Composable
fun MainView(
    showAlertDialog: MutableState<Boolean>,
    showDLevel: MutableState<Boolean>,
    gameLevel: MutableState<String>,
    isPortrait: Boolean
) {
    About(showAlertDialog, isPortrait)
    GameMode(showDLevel,gameLevel)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = if (isPortrait) 150.dp else 20.dp)
            .then(if (showAlertDialog.value) Modifier.blur(7.dp) else Modifier)
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_dice),
            contentDescription = "main dice",
            modifier = Modifier
                .size(if (isPortrait) 350.dp else 150.dp)
                .padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Let the Dice Decide Your Fate...!",
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(if (isPortrait) 50.dp else 25.dp))
        ButtonSection(showAlertDialog,gameLevel)
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isPortrait){10.dp}else{5.dp}), horizontalArrangement = Arrangement.End
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_settings_24),
                tint = Color.Black,
                contentDescription = "setting",
                modifier = Modifier
                    .size(35.dp)
                    .clickable {
                         showDLevel.value=true
                    })
        }

    }
}

@Composable
fun ButtonSection(showAlertDialog: MutableState<Boolean>,gameLevel:MutableState<String>) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            onClick = {
                val intent = Intent(context, GamePlayActivity::class.java)
                intent.putExtra("gameLevel", gameLevel.value)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF067EBF)
            ),
            modifier = Modifier
                .height(50.dp)
                .width(200.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
        ) {
            Text(
                "New Game",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        ElevatedButton(
            onClick = {
                showAlertDialog.value = true
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color(0xFF067EBF)
            ),
            modifier = Modifier
                .height(50.dp)
                .width(200.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp)
        ) {
            Text(
                "About",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp
            )
        }

    }
}

@Composable
fun About(showAlertDialog: MutableState<Boolean>, isPortrait: Boolean) {

    if (showAlertDialog.value) {
        AlertDialog(
            onDismissRequest = { showAlertDialog.value = false },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        shape = CircleShape,
                        border = BorderStroke(2.dp, Color.Black),
                        modifier = Modifier
                            .size(if (isPortrait) 150.dp else 90.dp)

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "profile", contentScale = ContentScale.Crop
                        )
                    }
                    if (isPortrait) {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    Text(
                        "Student ID : w1871523",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = if (isPortrait) 18.sp else 12.sp
                    )
                    if (isPortrait) {
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    Text(
                        "Name : Avishka Pramuditha",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = if (isPortrait) 18.sp else 12.sp
                    )
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.",
                        fontFamily = FontFamily.Serif,
                        fontSize = if (isPortrait) 15.sp else 10.sp,
                        style = TextStyle(
                            textAlign = TextAlign.Justify
                        )
                    )


                }

            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ElevatedButton(
                        onClick = { showAlertDialog.value = false },
                        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color(0xFF067EBF)
                        ),
                    ) {
                        Text(
                            "Done",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = if (isPortrait) 18.sp else 13.sp,
                        )

                    }
                }

            },

            containerColor = Color(0xFFD9EAFD),
            modifier = Modifier
                .padding(bottom = 15.dp, top = if (isPortrait) 0.dp else 5.dp)
                .width(if (isPortrait) 350.dp else 400.dp),
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

}

@Composable
fun GameMode(showDLevel:MutableState<Boolean>,gameLevel:MutableState<String>) {

    val levels = listOf("Easy", "Hard")
    var selectedLevel by remember { mutableStateOf(levels[0]) }

    if (showDLevel.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    "Choose The Difficulty Level",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {

                    levels.forEach { level ->

                        Row(modifier = Modifier.fillMaxWidth()) {
                            RadioButton(
                                onClick = {
                                    selectedLevel = level
                                }, selected = selectedLevel == level, colors = RadioButtonColors(
                                    selectedColor = Color(0xFF067EBF),
                                    unselectedColor = Color.Black,
                                    disabledSelectedColor = Color.Black,
                                    disabledUnselectedColor = Color.Black
                                ), modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                level,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp, modifier = Modifier.padding(top = 14.dp)
                            )
                        }
                    }

                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ElevatedButton(
                        onClick = {
                                gameLevel.value=selectedLevel
                                showDLevel.value = false
                            },
                        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color(0xFF067EBF)
                        ),
                    ) {
                        Text(
                            "Done",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                        )

                    }
                }

            },

            containerColor = Color(0xFFD9EAFD)
        )
    }

}
