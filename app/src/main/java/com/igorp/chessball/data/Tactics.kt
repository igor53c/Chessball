package com.igorp.chessball.data

import com.igorp.chessball.data.firebase.entities.Field
import com.igorp.chessball.data.firebase.entities.Player

object Tactics {

    val tactics = listOf("5-4-1", "5-3-2", "5-2-3", "4-5-1", "4-4-2", "4-3-3", "3-5-2", "3-4-3")

    fun getPlayers(tactic: Int, pos1: Boolean): List<Player> {
        val position: List<Field>
        var id = 0
        if(pos1) {
            position = getPosition1(
                Character.getNumericValue(tactics[tactic][0]),
                Character.getNumericValue(tactics[tactic][2]),
                Character.getNumericValue(tactics[tactic][4])
            )
        } else {
            id = 20
            position = getPosition2(
                Character.getNumericValue(tactics[tactic][0]),
                Character.getNumericValue(tactics[tactic][2]),
                Character.getNumericValue(tactics[tactic][4])
            )
        }
        val players = mutableListOf<Player>()
        for (i in 0..10) {
            when(i) {
                0 -> players.add(Player(position[i], position[i], false, id + i, true))
                1 -> players.add(Player(position[i], position[i], false, id + i, false))
                2 -> players.add(Player(position[i], position[i], false, id + i, false))
                3 -> players.add(Player(position[i], position[i], false, id + i, false))
                4 -> players.add(Player(position[i], position[i], false, id + i, false))
                5 -> players.add(Player(position[i], position[i], false, id + i, false))
                6 -> players.add(Player(position[i], position[i], false, id + i, false))
                7 -> players.add(Player(position[i], position[i], false, id + i, false))
                8 -> players.add(Player(position[i], position[i], false, id + i, false))
                9 -> players.add(Player(position[i], position[i], false, id + i, false))
                10 -> players.add(Player(position[i], position[i], pos1, id + i, false))
            }
        }
        return players
    }

    private fun getPosition1(def: Int, mid: Int, frw: Int): List<Field> {
        val position = mutableListOf<Field>()

        position.add(Field(5, 2))

        when(def) {
            3 -> {
                position.add(Field(3, 4))
                position.add(Field(5, 4))
                position.add(Field(7, 4))
            }
            4 -> {
                position.add(Field(2, 4))
                position.add(Field(4, 4))
                position.add(Field(6, 4))
                position.add(Field(8, 4))
            }
            5 -> {
                position.add(Field(1, 4))
                position.add(Field(3, 4))
                position.add(Field(5, 4))
                position.add(Field(7, 4))
                position.add(Field(9, 4))
            }
        }
        when(mid) {
            2 -> {
                position.add(Field(4, 6))
                position.add(Field(6, 6))
            }
            3 -> {
                position.add(Field(3, 6))
                position.add(Field(5, 6))
                position.add(Field(7, 6))
            }
            4 -> {
                position.add(Field(2, 6))
                position.add(Field(4, 6))
                position.add(Field(6, 6))
                position.add(Field(8, 6))
            }
            5 -> {
                position.add(Field(1, 6))
                position.add(Field(3, 6))
                position.add(Field(5, 6))
                position.add(Field(7, 6))
                position.add(Field(9, 6))
            }
        }
        when(frw) {
            1 -> {}
            2 -> { position.add(Field(5, 8)) }
            3 -> {
                position.add(Field(4, 8))
                position.add(Field(6, 8))
            }
        }

        position.add(Field(5, 9))

        return position
    }

    private fun getPosition2(def: Int, mid: Int, frw: Int): List<Field> {
        val position = mutableListOf<Field>()

        position.add(Field(5, 16))

        when(def) {
            3 -> {
                position.add(Field(3, 14))
                position.add(Field(5, 14))
                position.add(Field(7, 14))
            }
            4 -> {
                position.add(Field(2, 14))
                position.add(Field(4, 14))
                position.add(Field(6, 14))
                position.add(Field(8, 14))
            }
            5 -> {
                position.add(Field(1, 14))
                position.add(Field(3, 14))
                position.add(Field(5, 14))
                position.add(Field(7, 14))
                position.add(Field(9, 14))
            }
        }
        when(mid) {
            2 -> {
                position.add(Field(4, 12))
                position.add(Field(6, 12))
            }
            3 -> {
                position.add(Field(3, 12))
                position.add(Field(5, 12))
                position.add(Field(7, 12))
            }
            4 -> {
                position.add(Field(2, 12))
                position.add(Field(4, 12))
                position.add(Field(6, 12))
                position.add(Field(8, 12))
            }
            5 -> {
                position.add(Field(1, 12))
                position.add(Field(3, 12))
                position.add(Field(5, 12))
                position.add(Field(7, 12))
                position.add(Field(9, 12))
            }
        }
        when(frw) {
            1 -> { position.add(Field(5, 11)) }
            2 -> {
                position.add(Field(3, 10))
                position.add(Field(7, 10))
            }
            3 -> {
                position.add(Field(5, 11))
                position.add(Field(3, 10))
                position.add(Field(7, 10))
            }
        }

        return position
    }

    fun getFields(): List<Field> {
        val list = mutableListOf<Field>()
        for (row in 17 downTo 1) {
            for (column in 1 until 10) {
                list.add(Field(column, row))
            }
        }
        return list
    }

    fun compareFields(field1: Field?, field2: Field?) : Boolean {
        var state = false
        if(field1 != null && field2 != null && field1.column != null && field1.row != null &&
            field1.column!! == field2.column && field1.row!! == field2.row
        ) {
            state = true
        }
        return state
    }
}