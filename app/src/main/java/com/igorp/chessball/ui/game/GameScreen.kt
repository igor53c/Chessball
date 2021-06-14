package com.igorp.chessball.ui.game

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.SportsSoccer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.igorp.chessball.data.Tactics.compareFields
import com.igorp.chessball.data.Tactics.getFields
import com.igorp.chessball.data.firebase.entities.Ball
import com.igorp.chessball.data.firebase.entities.Player
import kotlin.math.abs

@ExperimentalFoundationApi
@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltNavGraphViewModel()
) {
    val fields = getFields()

    val isCompetitor1 by remember { mutableStateOf(viewModel.myPreference.getIsCompetitor1()) }

    var clickedPlayer by remember { mutableStateOf<Player?>(null) }

    var clickablePlayerEnabled by remember { mutableStateOf(true) }

    var longBall by remember { mutableStateOf(false) }

    var clickedBall by remember { mutableStateOf<Ball?>(null) }

    var clickableBallEnabled by remember { mutableStateOf(true) }

    var clickedBox by remember { mutableStateOf(false) }

    val listOfPlayersHost = viewModel.listOfPlayers.value

    val listOfPlayersGuest = viewModel.opponentPlayers.value

    var ball = viewModel.ball.value

    LazyVerticalGrid(cells = GridCells.Fixed(9)) {
        items(fields.size) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val color: Color = when (it) {
                    0, 1, 7, 8, 144, 145, 151, 152 -> {
                        Color.Red
                    }
                    2, 3, 4, 5, 6, 146, 147, 148, 149, 150 -> {
                        Color.White
                    }
                    else -> {
                        Color.Green
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(1.dp, Color.LightGray)
                        .background(color = color)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable {
                            if (clickedPlayer != null) {
                                if (
                                    fields[it].row != 1 && fields[it].row != 17 &&
                                    fields[it].playerId == null &&
                                    clickedPlayer!!.previousPosition?.column!! + 1 >= fields[it].column!! &&
                                    clickedPlayer!!.previousPosition?.column!! - 1 <= fields[it].column!! &&
                                    clickedPlayer!!.previousPosition?.row!! + 1 >= fields[it].row!! &&
                                    clickedPlayer!!.previousPosition?.row!! - 1 <= fields[it].row!!
                                ) {
                                    clickedPlayer!!.currentPosition = fields[it]
                                    fields[it].playerId = clickedPlayer!!.id
                                    clickedBox = true
                                }
                            }
                            if (clickedBall != null) {
                                if (
                                    !(fields[it].row == 1 && fields[it].column == 1) &&
                                    !(fields[it].row == 1 && fields[it].column == 2) &&
                                    !(fields[it].row == 1 && fields[it].column == 8) &&
                                    !(fields[it].row == 1 && fields[it].column == 9) &&
                                    !(fields[it].row == 17 && fields[it].column == 1) &&
                                    !(fields[it].row == 17 && fields[it].column == 2) &&
                                    !(fields[it].row == 17 && fields[it].column == 8) &&
                                    !(fields[it].row == 17 && fields[it].column == 9) &&
                                    (clickedBall!!.previousPosition?.column!! == fields[it].column!! ||
                                            clickedBall!!.previousPosition?.row!! == fields[it].row!! ||
                                            (abs(clickedBall!!.previousPosition?.row!! - fields[it].row!!)
                                                .equals(abs(clickedBall!!.previousPosition?.column!! - fields[it].column!!))))
                                ) {
                                    clickedBall!!.currentPosition = fields[it]
                                    clickedBox = true
                                }
                            }
                        }
                ) {
                    when (it) {
                        0, 1, 7, 8, 144, 145, 151 -> {
                        }
                        152 -> {
                            Icon(
                                Icons.Default.PlayCircle,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        ball?.competitor1Control?.let { it1 ->
                                            viewModel.setTemporaryPlayers(listOfPlayersHost, it1)
                                        }
                                        ball?.let { it1 ->
                                            viewModel.setTemporaryBall(it1)
                                        }
                                    }
                            )
                        }
                        2, 3, 4, 5, 6, 146, 147, 148, 149, 150 -> {
                            if (ball != null) {
                                var iconBall = Icons.Default.SportsSoccer
                                if (clickedBall != null) {
                                    iconBall = Icons.TwoTone.SportsSoccer
                                    if(clickedBox)  {
                                        ball = clickedBall
                                        clickedBox = false
                                        clickedBall = null
                                        clickablePlayerEnabled = true
                                    }
                                }
                                if (compareFields(fields[it], ball!!.currentPosition)) {
                                    var enabled = false
                                    if (((isCompetitor1 &&
                                                ball!!.competitor1Control != null &&
                                                ball!!.competitor1Control == true) ||
                                                (!isCompetitor1 &&
                                                        ball!!.competitor1Control != null &&
                                                        !ball!!.competitor1Control!!))
                                    ) {
                                        if(clickableBallEnabled) enabled = true
                                    }
                                    var ballColor = Color.Red
                                    if(longBall) ballColor = Color.Blue
                                    Icon(
                                        iconBall,
                                        contentDescription = "Ball",
                                        tint = ballColor,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable(enabled = enabled) {
                                                if(clickedBall != null) {
                                                    longBall = if(!longBall) {
                                                        ball!!.inTheAir = true
                                                        clickedBall!!.inTheAir = true
                                                        true
                                                    } else {
                                                        ball!!.inTheAir = false
                                                        clickedBall!!.inTheAir = false
                                                        false
                                                    }
                                                    clickablePlayerEnabled = true
                                                    clickedBall = null
                                                } else {
                                                    clickablePlayerEnabled = false
                                                    clickedBall = ball
                                                }
                                                clickedPlayer = null
                                            }
                                    )
                                }
                            }
                        }
                        else -> {
                            fields[it].playerId = null
                            for ((num, player) in listOfPlayersHost.withIndex()) {
                                if (clickedPlayer != null && clickedPlayer!!.id == player.id) {
                                    if(clickedBox) {
                                        listOfPlayersHost[num].currentPosition =
                                            clickedPlayer!!.currentPosition
                                        player.currentPosition = clickedPlayer!!.currentPosition
                                        clickedBox = false
                                        clickedPlayer = null
                                        clickableBallEnabled = true
                                    }
                                }
                                if (compareFields(fields[it], player.currentPosition)) {
                                    fields[it].playerId = player.id
                                    var colorPlayer = Color.White
                                    var colorGoalkeeper = Color.Yellow
                                    if (!isCompetitor1) {
                                        colorPlayer = Color.Black
                                        colorGoalkeeper = Color.Gray
                                    }
                                    if (player.goalkeeper != null && player.goalkeeper) {
                                        colorPlayer = colorGoalkeeper
                                    }
                                    var icon = Icons.Default.Person
                                    if (clickedPlayer != null && clickedPlayer!!.id == player.id) {
                                        icon = Icons.TwoTone.Person
                                    }
                                    Icon(
                                        icon,
                                        contentDescription = "Player",
                                        tint = colorPlayer,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable(enabled = clickablePlayerEnabled) {
                                                clickedPlayer = if (clickedPlayer != null &&
                                                    clickedPlayer!!.id == player.id) {
                                                    clickableBallEnabled = true
                                                    null
                                                } else {
                                                    clickableBallEnabled = false
                                                    player
                                                }
                                                clickedBall = null
                                            }
                                    )
                                }
                            }
                            for (player in listOfPlayersGuest) {
                                if (compareFields(fields[it], player.currentPosition)) {
                                    fields[it].playerId = player.id
                                    var colorPlayer = Color.Black
                                    var colorGoalkeeper = Color.Gray
                                    if (!isCompetitor1) {
                                        colorPlayer = Color.White
                                        colorGoalkeeper = Color.Yellow
                                    }
                                    if (player.goalkeeper != null && player.goalkeeper) {
                                        colorPlayer = colorGoalkeeper
                                    }
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = "Player",
                                        tint = colorPlayer,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                            if (ball != null) {
                                var iconBall = Icons.Default.SportsSoccer
                                if (clickedBall != null) {
                                    iconBall = Icons.TwoTone.SportsSoccer
                                    if(clickedBox)  {
                                        ball = clickedBall
                                        clickedBox = false
                                        clickedBall = null
                                        clickablePlayerEnabled = true
                                    }
                                }
                                if (compareFields(fields[it], ball!!.currentPosition)) {
                                    var enabled = false
                                    if (((isCompetitor1 &&
                                                ball!!.competitor1Control != null &&
                                                ball!!.competitor1Control == true) ||
                                                (!isCompetitor1 &&
                                                        ball!!.competitor1Control != null &&
                                                        !ball!!.competitor1Control!!))
                                    ) {
                                        if(clickableBallEnabled) enabled = true
                                    }
                                    var ballColor = Color.Red
                                    if(ball!!.inTheAir == true) ballColor = Color.Blue
                                    Icon(
                                        iconBall,
                                        contentDescription = "Ball",
                                        tint = ballColor,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable(enabled = enabled) {
                                                 if(clickedBall != null) {
                                                     longBall = if(!longBall) {
                                                         ball!!.inTheAir = true
                                                         clickedBall!!.inTheAir = true
                                                         true
                                                     } else {
                                                         ball!!.inTheAir = false
                                                         clickedBall!!.inTheAir = false
                                                         false
                                                     }
                                                     clickablePlayerEnabled = true
                                                     clickedBall = null
                                                } else {
                                                    clickablePlayerEnabled = false
                                                     clickedBall = ball
                                                }
                                                clickedPlayer = null
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
