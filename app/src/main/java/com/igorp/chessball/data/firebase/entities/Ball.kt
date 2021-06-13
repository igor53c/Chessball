package com.igorp.chessball.data.firebase.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Ball (
    val previousPosition: Field? = null,
    var currentPosition: Field? = null,
    val targetPosition: Field? = null,
    var inTheAir: Boolean? = null,
    val competitor1Control: Boolean? = null
)