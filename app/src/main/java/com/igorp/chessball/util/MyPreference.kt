package com.igorp.chessball.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.igorp.chessball.util.Constants.SHARED_PREFERENCES_KEY_CLUB_ID
import com.igorp.chessball.util.Constants.SHARED_PREFERENCES_KEY_GAME
import com.igorp.chessball.util.Constants.SHARED_PREFERENCES_KEY_IS_COMPETITOR
import com.igorp.chessball.util.Constants.SHARED_PREFERENCES_KEY_NAME
import com.igorp.chessball.util.Constants.SHARED_PREFERENCES_KEY_SELECTED_TACTIC
import com.igorp.chessball.util.Constants.SHARED_PREFERENCES_PREFIX
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreference @Inject constructor(@ApplicationContext context : Context) {

    private val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_PREFIX, Context.MODE_PRIVATE)

    fun getName(): String? {
        return sharedPref.getString(SHARED_PREFERENCES_KEY_NAME, "")
    }
    fun setName(name: String?) {
        sharedPref.edit().putString(SHARED_PREFERENCES_KEY_NAME, name).apply()
    }
    fun getClubId(): Int {
        return sharedPref.getInt(SHARED_PREFERENCES_KEY_CLUB_ID, 0)
    }
    fun setClubId(id: Int) {
        sharedPref.edit().putInt(SHARED_PREFERENCES_KEY_CLUB_ID, id).apply()
    }
    fun getSelectedTactic(): Int {
        return sharedPref.getInt(SHARED_PREFERENCES_KEY_SELECTED_TACTIC, 0)
    }
    fun setSelectedTactic(number: Int) {
        sharedPref.edit().putInt(SHARED_PREFERENCES_KEY_SELECTED_TACTIC, number).apply()
    }
    fun getGame(): String? {
        return sharedPref.getString(SHARED_PREFERENCES_KEY_GAME, "")
    }
    fun setGame(game: String?) {
        sharedPref.edit().putString(SHARED_PREFERENCES_KEY_GAME, game).apply()
    }
    fun getIsCompetitor1(): Boolean {
        return sharedPref.getBoolean(SHARED_PREFERENCES_KEY_IS_COMPETITOR, true)
    }
    fun setIsCompetitor1(state: Boolean) {
        sharedPref.edit().putBoolean(SHARED_PREFERENCES_KEY_IS_COMPETITOR, state).apply()
    }
}