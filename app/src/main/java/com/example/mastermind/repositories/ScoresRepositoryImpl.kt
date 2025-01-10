package com.example.mastermind.repositories

import com.example.mastermind.dao.ScoreDao
import com.example.mastermind.entities.Score
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScoresRepositoryImpl @Inject constructor(
    private val scoreDao: ScoreDao
): ScoresRepository {
    override suspend fun insert(score: Score): Long {
        return scoreDao.insert(score)
    }
}
