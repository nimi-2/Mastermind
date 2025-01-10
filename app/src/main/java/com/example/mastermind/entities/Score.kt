package com.example.mastermind.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val scoreId: Long = 0,
    val playerId: Long,

    val score: Long = 0
)