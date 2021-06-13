package com.igorp.chessball.data.local.dao

import androidx.room.*
import com.igorp.chessball.data.local.entities.BallLocal

@Dao
interface BallLocalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBall(ball: BallLocal)

    @Delete
    fun deleteBall(ball: BallLocal)

    @Update
    fun updateBall(ball: BallLocal)
}