package com.example.mastermind.models

import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.ViewModel
import com.example.mastermind.entities.Score
import com.example.mastermind.repositories.PlayersRepository
import com.example.mastermind.repositories.ScoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    playersRepository: PlayersRepository,
    private val scoresRepository: ScoresRepository
) : ViewModel() {
    var playerId = playersRepository.getCurrentPlayerId()
    var score = mutableLongStateOf(0L)

    suspend fun savePlayerScore() {
        val playerIdValue = playerId.value ?: throw IllegalStateException("PlayerId is null")
        val score = Score(playerId = playerIdValue, score = score.longValue)
        scoresRepository.insert(score)
    }
}