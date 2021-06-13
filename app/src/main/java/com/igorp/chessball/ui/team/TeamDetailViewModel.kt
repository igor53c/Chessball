package com.igorp.chessball.ui.team

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.igorp.chessball.data.api.remote.responses.TeamDto
import com.igorp.chessball.repository.TeamRepository
import com.igorp.chessball.util.Resource
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    private val repository: TeamRepository
) : ViewModel() {

    suspend fun getTeamInfo(id: Int): Resource<TeamDto> {
        return repository.getTeamInfo(id)
    }
}