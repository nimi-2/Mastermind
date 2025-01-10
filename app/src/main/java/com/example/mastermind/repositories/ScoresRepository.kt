package com.example.mastermind.repositories

import com.example.mastermind.entities.Score

interface ScoresRepository {
    suspend fun insert(score: Score): Long
}