package com.example.w1871523_mad_cw1

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val configuration = LocalConfiguration.current
            val isPortrait = configuration.orientation
            Box() {
                if (isPortrait == Configuration.ORIENTATION_PORTRAIT) {
                    PortraitView()
                } else {
                    LandScapeView()
                }
            }

        }

    }
}


@Composable
fun PortraitView() {

    AlertDialog(
        onDismissRequest = {},
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    shape = CircleShape,
                    border = BorderStroke(2.dp, Color.Black),
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "profile", contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Student ID : w1871523",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    "Name : Avishka Pramuditha",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.",
                    fontFamily = FontFamily.Serif,
                    fontSize = 15.sp,
                    style = TextStyle(
                        textAlign = TextAlign.Justify
                    )
                )


            }

        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                ElevatedButton(onClick = {}, elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp), colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xFF067EBF)
                ),
                ) {
                    Text(
                        "Done",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp
                    )

                }
            }

        }, modifier = Modifier.padding(bottom = 15.dp)
    )








    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_dice),
            contentDescription = "main dice",
            modifier = Modifier
                .width(350.dp)
                .height(350.dp)
                .padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Let the Dice Decide Your Fate...!",
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(50.dp))
        ButtonSection()
        Spacer(modifier = Modifier.weight(1f))
        Setting()
    }
}

@Composable
fun LandScapeView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)//.background(Color.Blue)
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_dice),
            contentDescription = "main dice",
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .padding(start = 15.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Let the Dice Decide Your Fate...!",
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(25.dp))
        ButtonSection()
        Spacer(modifier = Modifier.weight(0.5f))
        Setting()
    }
}

@Composable
fun ButtonSection() {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            onClick = {},
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
fun Setting() {

    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        IconButton(
            onClick = {}, modifier = Modifier
                .size(40.dp)

        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = "Setting",
                Modifier
                    .size(40.dp)
            )
        }
    }

}

@Composable
fun About(show: Boolean) {

    AlertDialog(
        onDismissRequest = {},
        title = { Text("fgsdgdfhdfgdfgdgdgdfg") },
        text = {
            Text("gdfgsgsdg")
        },
        confirmButton = {}
    )
}
