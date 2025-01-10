package com.example.mastermind.containers

//package com.example.mastermind.containers
//
//import android.content.Context
//import com.example.mastermind.HighScoreDatabase
//import com.example.mastermind.repositories.PlayerScoresRepository
//import com.example.mastermind.repositories.PlayerScoresRepositoryImpl
//import com.example.mastermind.repositories.PlayersRepository
//import com.example.mastermind.repositories.PlayersRepositoryImpl
//import com.example.mastermind.repositories.ScoresRepository
//import com.example.mastermind.repositories.ScoresRepositoryImpl
//
//
//class AppDataContainer(private val context: Context) : AppContainer {
//    override val playersRepository: PlayersRepository by lazy {
//        PlayersRepositoryImpl(HighScoreDatabase.getDatabase(context).playerDao())
//    }
//    override val scoresRepository: ScoresRepository by lazy {
//        ScoresRepositoryImpl(HighScoreDatabase.getDatabase(context).scoreDao())
//    }
//    override val playerScoresRepository: PlayerScoresRepository by lazy {
//        PlayerScoresRepositoryImpl(HighScoreDatabase.getDatabase(context).playerScoreDao())
//    }
//}