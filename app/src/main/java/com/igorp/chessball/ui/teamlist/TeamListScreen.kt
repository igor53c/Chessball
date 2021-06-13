package com.igorp.chessball.ui.teamlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.igorp.chessball.data.api.remote.responses.TeamDto
import com.igorp.chessball.data.api.remote.responses.TeamListDto
import com.igorp.chessball.ui.theme.RobotoCondensed
import com.igorp.chessball.util.Resource

@Composable
fun TeamListScreen(
    leagueId: Int?,
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            TeamList(leagueId = leagueId, navController = navController)
        }
    }
}

@Composable
fun TeamList(
    leagueId: Int?,
    navController: NavController,
    viewModel: TeamListViewModel = hiltNavGraphViewModel()
) {
    val teamList = produceState<Resource<TeamListDto>>(initialValue = Resource.Loading()) {
        value = viewModel.getTeamList(leagueId!!)
    }.value

    if(teamList is Resource.Success) {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            if(teamList.data != null) {
                val itemCount = if(teamList.data.teams.size % 2 == 0) {
                    teamList.data.teams.size / 2
                } else {
                    teamList.data.teams.size / 2 + 1
                }
                items(itemCount) {
                    TeamRow(rowIndex = it, entries = teamList.data.teams, navController = navController)
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if(teamList is Resource.Loading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(teamList is Resource.Error) {
            if(teamList.message != null) {
                Text(teamList.message, color = Color.Red, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun TeamEntry(
    entry: TeamDto,
    navController: NavController,
    modifier: Modifier,
    viewModel: TeamListViewModel = hiltNavGraphViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {
                viewModel.myPreference.setClubId(entry.id)
                navController.navigate("start_screen")
            }
            .padding(top = 8.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        Column {
            Image(
                painter = rememberCoilPainter(
                    request = ImageRequest.Builder(LocalContext.current)
                    .data(entry.crestUrl)
                    .target {
                        viewModel.calcDominantColor(it) { color ->
                            dominantColor = color
                        }
                    }
                    .build(),
                    imageLoader = ImageLoader.Builder(LocalContext.current)
                        .componentRegistry {
                            add(SvgDecoder(LocalContext.current))
                        }
                        .build(),
                    fadeIn = true,
                ),
                contentDescription = entry.shortName,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = entry.shortName,
                fontFamily = RobotoCondensed,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun TeamRow(
    rowIndex: Int,
    entries: List<TeamDto>,
    navController: NavController
) {
    Column {
        Row {
            TeamEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2) {
                TeamEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}