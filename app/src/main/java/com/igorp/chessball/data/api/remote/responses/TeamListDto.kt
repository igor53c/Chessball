package com.igorp.chessball.data.api.remote.responses

import com.google.gson.annotations.SerializedName

data class TeamListDto (

    @SerializedName("teams")
    val teams: List<TeamDto> = listOf(),
)