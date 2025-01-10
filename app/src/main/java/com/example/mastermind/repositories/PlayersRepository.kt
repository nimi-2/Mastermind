package com.example.mastermind.repositories

import androidx.lifecycle.LiveData
import com.example.mastermind.entities.Player
import kotlinx.coroutines.flow.Flow

interface PlayersRepository {
    fun getAllPlayersStream(): Flow<List<Player>>
    fun getPlayerStream(id: Long): Flow<Player?>
    fun setCurrentPlayerId(id: Long)
    fun getCurrentPlayerId() : LiveData<Long>
    suspend fun getPlayersByEmail(email: String): List<Player>
    suspend fun insert(player: Player): Long
    suspend fun update(player: Player): Int
}