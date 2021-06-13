package com.igorp.chessball.ui.team

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.igorp.chessball.data.api.remote.responses.TeamDto
import com.igorp.chessball.util.Resource
import java.util.*

@Composable
fun TeamDetailScreen(
    dominantColor: Color,
    teamId: Int?,
    navController: NavController,
    topPadding: Dp = 20.dp,
    teamImageSize: Dp = 200.dp,
    viewModel: TeamDetailViewModel = hiltNavGraphViewModel()
) {
    val teamInfo = produceState<Resource<TeamDto>>(initialValue = Resource.Loading()) {
        value = viewModel.getTeamInfo(teamId!!)
    }.value
    Box(modifier = Modifier
        .fillMaxSize()
        .background(dominantColor)
        .padding(bottom = 16.dp)
    ) {
        TeamDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        TeamDetailStateWrapper(
            teamInfo = teamInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + teamImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + teamImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )
        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if(teamInfo is Resource.Success) {
                teamInfo.data?.crestUrl?.let {
                    Image(
                        painter = rememberCoilPainter(
                            request = ImageRequest.Builder(LocalContext.current)
                                .data(it)
                                .build(),
                            imageLoader = ImageLoader.Builder(LocalContext.current)
                                .componentRegistry {
                                    add(SvgDecoder(LocalContext.current))
                                }
                                .build(),
                            fadeIn = true,
                        ),
                        contentDescription = teamInfo.data.name,
                        modifier = Modifier
                            .size(teamImageSize)
                            .offset(y = topPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TeamDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun TeamDetailStateWrapper(
    teamInfo: Resource<TeamDto>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when(teamInfo) {
        is Resource.Success -> {
            TeamDetailSection(
                teamInfo = teamInfo.data!!,
                modifier = modifier
                    .offset(y = (-20).dp)
            )
        }
        is Resource.Error -> {
            Text(
                text = teamInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun TeamDetailSection(
    teamInfo: TeamDto,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
    ) {
        Text(
            text = teamInfo.name.capitalize(Locale.ROOT),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
        LazyColumn {
            itemsIndexed(
                items = teamInfo.squad,
                itemContent = { _, player ->
                    Text(text = "${player.position}  ${player.name}")
                }
            )
        }
    }
}