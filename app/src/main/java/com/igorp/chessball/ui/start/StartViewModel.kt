package com.igorp.chessball.ui.start

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.igorp.chessball.data.Tactics
import dagger.hilt.android.lifecycle.HiltViewModel
import com.igorp.chessball.data.api.remote.responses.TeamDto
import com.igorp.chessball.data.firebase.entities.*
import com.igorp.chessball.repository.TeamRepository
import com.igorp.chessball.util.MyPreference
import com.igorp.chessball.util.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val repository: TeamRepository,
    val myPreference: MyPreference
) : ViewModel() {

    val database = Firebase.database.reference

    private val _isWrited = MutableLiveData<Boolean>()
    var isWrited: LiveData<Boolean> = _isWrited

    var games = listOf<Game>()

    suspend fun getTeamInfo(id: Int): Resource<TeamDto> {
        return repository.getTeamInfo(id)
    }

    fun searchingForAnOpponent(name: String, clubId: Int, selectedTactic: Int) {
        val competitor = Competitor(Club(null, clubId), Tactics.tactics.get(selectedTactic))
        val ball = Ball(Field(5, 9), Field(5, 9), null, false, true)
        val game = Game(name, competitor, null, ball)

        viewModelScope.launch {
            games = getListOfGames()
            if(games.size == 0) {
                joinTheGameAsCompetitor1(name, selectedTactic, competitor, game)
            } else {
                var selected = false
                for (tempGame in games) {
                    if(game.competitor2 == null) {
                        joinTheGameAsCompetitor2(name, selectedTactic, competitor, tempGame)
                        selected = true
                    }
                }
                if(!selected) {
                    joinTheGameAsCompetitor1(name, selectedTactic, competitor, game)
                }
            }
        }
    }

    fun joinTheGameAsCompetitor2(name: String, selectedTactic: Int, competitor: Competitor, game: Game) {
        database.child("players").child(name).setValue(competitor)
            .addOnSuccessListener {
                competitor.club?.players = Tactics.getPlayers(selectedTactic, false)
                game.competitor2 = competitor

                game.name?.let { it1 ->
                    database.child("games").child(it1).setValue(game)
                        .addOnSuccessListener {
                            myPreference.setIsCompetitor1(false)
                            myPreference.setGame(game.name)
                            _isWrited.postValue(true)
                        }
                        .addOnFailureListener { _isWrited.postValue(false) }
                }
            }
            .addOnFailureListener { _isWrited.postValue(false) }
    }

    fun joinTheGameAsCompetitor1(name: String, selectedTactic: Int, competitor: Competitor, game: Game) {
        database.child("players").child(name).setValue(competitor)
            .addOnSuccessListener {
                competitor.club?.players = Tactics.getPlayers(selectedTactic, true)

                database.child("games").child(name).setValue(game)
                    .addOnSuccessListener {
                        myPreference.setIsCompetitor1(true)
                        myPreference.setGame(name)
                        _isWrited.postValue(true)
                    }
                    .addOnFailureListener { _isWrited.postValue(false) }
            }
            .addOnFailureListener { _isWrited.postValue(false) }
    }

    suspend fun getListOfGames() : List<Game> {
        val allGames = database.child("games").get()

        val games = try {
            allGames.await().children.map { snapShot ->
                snapShot.getValue(Game::class.java)!!
            }
        } catch (exception: Exception) {
            emptyList()
        }
        return games
    }
}