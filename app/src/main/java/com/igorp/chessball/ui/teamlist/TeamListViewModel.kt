package com.igorp.chessball.ui.teamlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import com.igorp.chessball.data.api.remote.responses.TeamListDto
import com.igorp.chessball.repository.TeamRepository
import com.igorp.chessball.util.MyPreference
import com.igorp.chessball.util.Resource
import javax.inject.Inject

@HiltViewModel
class TeamListViewModel @Inject constructor(
    private val repository: TeamRepository,
    val myPreference: MyPreference
) : ViewModel() {

    suspend fun getTeamList(leagueId: Int?): Resource<TeamListDto> {
        return repository.getTeamList(leagueId)
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}