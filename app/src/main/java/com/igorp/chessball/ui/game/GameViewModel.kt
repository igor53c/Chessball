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
import com.igorp.chessball.data.Tactics.compareFields
import com.igorp.chessball.data.firebase.entities.Ball
import com.igorp.chessball.data.firebase.entities.Player
import com.igorp.chessball.util.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val myPreference: MyPreference
) : ViewModel() {

    val database = Firebase.database.reference

    val opponentPlayers : MutableState<List<Player>> = mutableStateOf(listOf())

    val opponentTemporaryPlayers : MutableState<List<Player>> = mutableStateOf(listOf())

    val listOfPlayers : MutableState<List<Player>> = mutableStateOf(listOf())

    val ball : MutableState<Ball?> = mutableStateOf(null)

    val opponentName : MutableState<String?> = mutableStateOf(null)

    val isOpponentPlayed : MutableState<Boolean?> = mutableStateOf(null)

    val name = myPreference.getName()

    val game = myPreference.getGame()

    val isCompetitor1 = myPreference.getIsCompetitor1()

    init {
        getListOfPlayers()
        getOpponentPlayers()
        getBall()
        getIsOpponentPlayed()
        getOpponentName()
    }

    fun getListOfPlayers() {
        val competitor: String = if (isCompetitor1) "competitor1" else "competitor2"
        val allPlayers = game?.let {
            database.child("games").child(it).child(competitor)
                .child("club").child("players")
        }

        if (allPlayers != null) {
            allPlayers.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list = mutableListOf<Player>()
                    for (postSnapshot in dataSnapshot.children) {
                        val player = postSnapshot.getValue(Player::class.java)
                        if (player != null) {
                            list.add(player)
                        }
                    }
                    listOfPlayers.value = list
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    fun getBall() {
        val ballTemp = game?.let {
            database.child("games").child(it).child("ball")
        }
        ballTemp?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                ball.value = dataSnapshot.getValue(Ball::class.java)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun getOpponentPlayers() {
        val competitor: String = if (!isCompetitor1) "competitor1" else "competitor2"
        val allPlayers = game?.let {
            database.child("games").child(it).child(competitor)
                .child("club").child("players")
        }

        if (allPlayers != null) {
            allPlayers.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list = mutableListOf<Player>()
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

    fun getOpponentTemporaryPlayers() {
        val players = opponentName.value?.let {
            database.child("players").child(it).child("club")
                .child("players")
        }
        if (players != null) {
            players.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list = mutableListOf<Player>()
                    for (postSnapshot in dataSnapshot.children) {
                        val player = postSnapshot.getValue(Player::class.java)
                        if (player != null) {
                            list.add(player)
                        }
                    }
                    opponentTemporaryPlayers.value = list
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    fun setTemporaryPlayers(players: List<Player>, competitor1Control: Boolean) {
        val competitor: String = if (isCompetitor1) "competitor1" else "competitor2"
        if (game != null && name != null) {
            database.child("players").child(name).child("club")
                .child("players").setValue(players)
                .addOnSuccessListener {  }
                .addOnFailureListener {  }
            database.child("games").child(game).child(competitor)
                .child("played").setValue(true)
                .addOnSuccessListener {  }
                .addOnFailureListener {  }
        }
        if(isOpponentPlayed.value == true) {
            setAllPlayers(players, competitor1Control)
        }
    }

    private fun setAllPlayers(players: List<Player>, competitor1Control: Boolean) {
        val competitor: String = if (isCompetitor1) "competitor1" else "competitor2"
        val competitorOpponent: String = if (!isCompetitor1) "competitor1" else "competitor2"
        if((isCompetitor1 && competitor1Control) || (!isCompetitor1 && !competitor1Control)) {
            if (game != null) {
                val pl = setPreviousPositionAsCurrentPosition(opponentTemporaryPlayers.value)
                database.child("games").child(game).child(competitorOpponent)
                    .child("club").child("players").setValue(pl)
                    .addOnSuccessListener {  }
                    .addOnFailureListener {  }
                val playersWithBall =
                    checkOverlappingPosition(players, opponentTemporaryPlayers.value)
                val pl2 = setPreviousPositionAsCurrentPosition(playersWithBall)
                database.child("games").child(game).child(competitor)
                    .child("club").child("players").setValue(pl2)
                    .addOnSuccessListener {  }
                    .addOnFailureListener {  }
            }
        } else {
            if (game != null) {
                val pl = setPreviousPositionAsCurrentPosition(players)
                database.child("games").child(game).child(competitor)
                    .child("club").child("players").setValue(pl)
                    .addOnSuccessListener {  }
                    .addOnFailureListener {  }
                val playersWithBall =
                    checkOverlappingPosition(opponentTemporaryPlayers.value, players)
                val pl2 = setPreviousPositionAsCurrentPosition(playersWithBall)
                database.child("games").child(game).child(competitorOpponent)
                    .child("club").child("players").setValue(pl2)
                    .addOnSuccessListener {  }
                    .addOnFailureListener {  }
            }
        }
    }

    private fun setPreviousPositionAsCurrentPosition(players: List<Player>) : List<Player> {
        for(i in 0 until players.size) {
            players[i].previousPosition?.column = players[i].currentPosition?.column
            players[i].previousPosition?.row = players[i].currentPosition?.row
        }
        return players
    }

    private fun checkOverlappingPosition(
        playersWithBall: List<Player>, playersWithoutBall: List<Player>) : List<Player> {
        for(playerWithBall in playersWithBall) {
            for(playerWithoutBall in playersWithoutBall) {
                if(compareFields(playerWithBall.currentPosition,
                        playerWithoutBall.currentPosition)) {
                    playerWithBall.currentPosition?.column = playerWithBall.previousPosition?.column
                    playerWithBall.currentPosition?.row = playerWithBall.previousPosition?.row
                    //checkOverlappingPosition()
                }
            }
        }
        return playersWithBall
    }

    fun setTemporaryBall(ball: Ball) {
        if (((isCompetitor1 && ball.competitor1Control == true) ||
                    (!isCompetitor1 && !ball.competitor1Control!!))
        ) {
            if (game != null) {
                database.child("games").child(game)
                    .child("ballTemporary").setValue(ball)
                    .addOnSuccessListener {  }
                    .addOnFailureListener {  }
            }
        }
    }

    fun getIsOpponentPlayed() {
        val competitor: String = if (!isCompetitor1) "competitor1" else "competitor2"
        val isPlayed = game?.let {
            database.child("games").child(it).child(competitor)
                .child("played")
        }
        if (isPlayed != null) {
            isPlayed.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    isOpponentPlayed.value = dataSnapshot.getValue(Boolean::class.java)
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    fun getOpponentName() {
        val competitor: String = if (!isCompetitor1) "competitor1" else "competitor2"
        val opponentNameTemp = game?.let {
            database.child("games").child(it).child(competitor)
                .child("name")
        }
        if (opponentNameTemp != null) {
            opponentNameTemp.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    opponentName.value = dataSnapshot.getValue(String::class.java)
                    getOpponentTemporaryPlayers()
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}