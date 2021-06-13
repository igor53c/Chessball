package com.igorp.chessball.repository

import dagger.hilt.android.scopes.ActivityScoped
import com.igorp.chessball.data.api.remote.TeamApi
import com.igorp.chessball.data.api.remote.responses.TeamDto
import com.igorp.chessball.data.api.remote.responses.TeamListDto
import com.igorp.chessball.util.Resource
import javax.inject.Inject


@ActivityScoped
class TeamRepository @Inject constructor(
    private val api: TeamApi
) {

    suspend fun getTeamList(leagueId: Int?): Resource<TeamListDto> {
        val response = try {
            api.getTeamList(id = leagueId.toString())
        } catch(e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    suspend fun getTeamInfo(teamId: Int): Resource<TeamDto> {
        val response = try {
            api.getTeamInfo(id = teamId.toString())
        } catch(e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }
}
