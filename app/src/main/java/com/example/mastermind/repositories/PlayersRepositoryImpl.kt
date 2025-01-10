package com.example.mastermind.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mastermind.dao.PlayerDao
import com.example.mastermind.entities.Player
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayersRepositoryImpl @Inject constructor(
    private val playerDao: PlayerDao
) : PlayersRepository {
    private val currentPlayerId = MutableLiveData<Long>()
    override fun getAllPlayersStream(): Flow<List<Player>> {
        TODO("Not yet implemented")
    }

    override fun getPlayerStream(playerId: Long): Flow<Player?> =
        playerDao.getPlayerStream(playerId)

    override fun setCurrentPlayerId(id: Long) {
        currentPlayerId.postValue(id)
    }

    override fun getCurrentPlayerId(): LiveData<Long> {
        return currentPlayerId
    }

    override suspend fun getPlayersByEmail(email: String): List<Player> =
        playerDao.getPlayersByEmail(email)
    override suspend fun insert(player: Player): Long = playerDao.insert(player)
    override suspend fun update(player: Player): Int = playerDao.update(player)
}