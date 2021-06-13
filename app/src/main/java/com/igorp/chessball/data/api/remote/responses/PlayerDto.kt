package com.igorp.chessball.data.api.remote.responses

import com.google.gson.annotations.SerializedName

data class PlayerDto (

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("position")
    val position: String,

    @SerializedName("dateOfBirth")
    val dateOfBirth: String,

    @SerializedName("countryOfBirth")
    val countryOfBirth: String,

    @SerializedName("nationality")
    val nationality: String,

    @SerializedName("shirtNumber")
    val shirtNumber: Int?,

    @SerializedName("role")
    val role: String
    )