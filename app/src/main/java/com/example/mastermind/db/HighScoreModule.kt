package com.example.mastermind.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mastermind.dao.PlayerDao
import com.example.mastermind.dao.PlayerScoreDao
import com.example.mastermind.dao.ScoreDao
import com.example.mastermind.entities.Score
import com.example.mastermind.entities.Player

@Database(
    entities = [Score::class, Player::class],
    version = 1,
    exportSchema = false
)
abstract class HighScoreDatabase: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun playerScoreDao(): PlayerScoreDao
    abstract fun scoreDao(): ScoreDao


    companion object {
        @Volatile
        private var Instance: HighScoreDatabase? = null
        fun getDatabase(context: Context): HighScoreDatabase {
            return Room.databaseBuilder(
                context,
                HighScoreDatabase::class.java,
                "high_score_database"
            )
                .build().also { Instance = it }
        }
    }
}
