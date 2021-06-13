package com.igorp.chessball.data.firebase.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Club (
    var players: List<Player>? = null,
    val id: Int? = null
)