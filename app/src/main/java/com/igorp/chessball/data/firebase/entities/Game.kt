package com.igorp.chessball.data.firebase.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Game (
    val name: String? = null,
    val competitor1: Competitor? = null,
    var competitor2: Competitor? = null,
    val ball: Ball? = null
)