package com.igorp.chessball.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ball_table")
data class BallLocal (
    val previousPosition: String? = null,
    val currentPosition: String? = null,
    val targetPosition: String? = null,
    val inTheAir: Boolean? = null,
    val competitor1Control: Boolean? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}