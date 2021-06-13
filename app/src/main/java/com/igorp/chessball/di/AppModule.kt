package com.igorp.chessball.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.igorp.chessball.data.api.remote.TeamApi
import com.igorp.chessball.data.local.LocalDatabase
import com.igorp.chessball.repository.FirebaseRepository
import com.igorp.chessball.repository.TeamRepository
import com.igorp.chessball.util.Constants.BASE_URL
import com.igorp.chessball.util.Constants.DATABASE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTeamRepository(
        api: TeamApi
    ) = TeamRepository(api)

    @Singleton
    @Provides
    fun provideFirebaseRepository() = FirebaseRepository()

    @Singleton
    @Provides
    fun provideTeamApi(): TeamApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(TeamApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, LocalDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideBallLocalDao(db: LocalDatabase) = db.getBallLocalDao()
}