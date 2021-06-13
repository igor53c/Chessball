package com.igorp.chessball.ui.game

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.igorp.chessball.data.firebase.entities.Ball
import com.igorp.chessball.data.firebase.entities.Player
import com.igorp.chessball.util.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val myPreference: MyPreference
) : ViewModel() {

    val database = Firebase.database.reference

    val opponentPlayers : MutableState<List<Player>> = mutableStateOf(listOf())
    val ball : MutableState<Ball?> = mutableStateOf(null)

    init {
        getOpponentPlayers()
        getBall()
    }

    suspend fun getListOfPlayers(name: String, state: Boolean): List<Player> {
        val competitor: String = if (state) "competitor1" else "competitor2"
        val allPlayers = database.child("games").child(name).child(competitor)
            .child("club").child("players").get()

        return try {
            allPlayers.await().children.map { snapShot ->
                snapShot.getValue(Player::class.java)!!
            }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    fun getBall() {
        val name = myPreference.getGame()
        val ballTemp = name?.let {
            database.child("games").child(it).child("ball")
        }
        ballTemp?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                ball.value = dataSnapshot.getValue(Ball::class.java)

                Log.d("prosao", "ball: " + ball)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getOpponentPlayers() {
        val name = myPreference.getGame()
        val state = myPreference.getIsCompetitor1()
        val competitor: String = if (!state) "competitor1" else "competitor2"
        val allPlayers = name?.let {
            database.child("games").child(it).child(competitor)
                .child("club").child("players")
        }

        val list = mutableListOf<Player>()

        if (allPlayers != null) {
            allPlayers.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val player = postSnapshot.getValue(Player::class.java)
                        if (player != null) {
                            list.add(player)
                        }
                    }
                    opponentPlayers.value = list
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}