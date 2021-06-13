package com.igorp.chessball.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.igorp.chessball.ui.game.GameScreen
import com.igorp.chessball.ui.start.StartScreen
import com.igorp.chessball.ui.team.TeamDetailScreen
import com.igorp.chessball.ui.teamlist.TeamListScreen
import com.igorp.chessball.ui.theme.ChessballTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChessballTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "start_screen"
                ) {
                    composable("start_screen") {
                        StartScreen(navController = navController)
                    }
                    composable(
                        "team_list_screen/{leagueId}",
                        arguments = listOf(
                            navArgument("leagueId") {
                                type = NavType.IntType
                            }
                        )
                    ) {
                        val leagueId = remember {
                            it.arguments?.getInt("leagueId")
                        }
                        TeamListScreen(
                            leagueId = leagueId,
                            navController = navController
                        )
                    }
                    composable("game_screen") {
                        GameScreen(navController = navController)
                    }
                    composable(
                        "team_detail_screen/{dominantColor}/{teamId}",
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("teamId") {
                                type = NavType.IntType
                            }
                        )
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val teamId = remember {
                            it.arguments?.getInt("teamId")
                        }
                        TeamDetailScreen(
                            dominantColor = dominantColor,
                            teamId = teamId,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
