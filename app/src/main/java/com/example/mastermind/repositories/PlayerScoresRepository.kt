package com.example.mastermind.repositories


import com.example.mastermind.entities.PlayerWithScore
import kotlinx.coroutines.flow.Flow

interface PlayerScoresRepository {
    fun loadPlayersWithScores(): Flow<List<PlayerWithScore>>
}
