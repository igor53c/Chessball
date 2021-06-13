package com.igorp.chessball.data.firebase.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Player (
    var previousPosition: Field? = null,
    var currentPosition: Field? = null,
    val hasTheBall: Boolean? = null,
    val id: Int? = null,
    val goalkeeper: Boolean? = null
)