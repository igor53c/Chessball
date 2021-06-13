package com.igorp.chessball.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.igorp.chessball.data.local.dao.BallLocalDao
import com.igorp.chessball.data.local.entities.BallLocal

@Database(entities = [BallLocal::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun getBallLocalDao(): BallLocalDao
}