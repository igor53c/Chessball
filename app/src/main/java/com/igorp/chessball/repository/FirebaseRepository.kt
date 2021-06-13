package com.igorp.chessball.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.igorp.chessball.data.firebase.entities.Player
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.tasks.await

@ActivityScoped
class FirebaseRepository {

    val database = Firebase.database.reference

    suspend fun getListOfPlayers(name: String) : List<Player> {
        val allPlayers = database.child("games").child("game: " + name).child("competitor1")
            .child("club").child("players").get()

        val players = try {
            allPlayers.await().children.map { snapShot ->
                snapShot.getValue(Player::class.java)!!
            }
        } catch (exception: Exception) {
            emptyList()
        }
        return players
    }
}