package com.example.mastermind.models

import androidx.lifecycle.ViewModel
import com.example.mastermind.entities.PlayerWithScore
import com.example.mastermind.repositories.PlayerScoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val playerScoresRepository: PlayerScoresRepository
) : ViewModel() {

    fun loadPlayerScores(): Flow<List<PlayerWithScore>>{
        return playerScoresRepository.loadPlayersWithScores()
            .map { players ->
                players.sortedBy { it.score }
            }
    }
}