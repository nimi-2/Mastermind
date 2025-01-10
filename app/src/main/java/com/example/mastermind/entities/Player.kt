package com.example.mastermind.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val playerId: Long = 0,
    var name: String,
    var email: String,
    var profileImageUri: String? = null
)