package com.igorp.chessball.data.firebase.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Competitor (
    val club: Club? = null,
    val formation: String? = null
)