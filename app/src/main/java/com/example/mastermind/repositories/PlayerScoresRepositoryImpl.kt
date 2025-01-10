package com.example.mastermind.repositories

import com.example.mastermind.dao.PlayerScoreDao
import com.example.mastermind.entities.PlayerWithScore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerScoresRepositoryImpl @Inject constructor(
    private val playerScoreDao: PlayerScoreDao
): PlayerScoresRepository {
    override fun loadPlayersWithScores(): Flow<List<PlayerWithScore>> =  playerScoreDao.loadPlayersWithScores();

}