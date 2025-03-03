package com.example.w1871523_mad_cw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class GamePlayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainView()
        }

    }
}

@Composable
fun MainView() {
    Box{
        Image(
            painter = painterResource(id = R.drawable.play_background),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        ScoreBoard()
    }

}

@Composable
fun ScoreBoard() {
    Card(
        modifier = Modifier
            .height(240.dp)
            .fillMaxWidth()
            .padding(top = 60.dp, end = 15.dp, start = 15.dp), shape = RoundedCornerShape(20.dp)
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
                        .height(180.dp)
                        .width(215.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 15.dp, start = 20.dp, bottom = 10.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
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
                            "You : 0    /    Bot : 0",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 25.dp)
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
                                    ": 000", fontSize = 25.sp,
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
                                    ": 000", fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
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
                            Text("101", fontWeight = FontWeight.Bold, fontSize = 25.sp)

                        }
                    }
                }
            }


        }
    }
}