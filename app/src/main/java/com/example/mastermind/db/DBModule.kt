package com.example.mastermind.db

import android.content.Context
import com.example.mastermind.dao.PlayerDao
import com.example.mastermind.dao.PlayerScoreDao
import com.example.mastermind.dao.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    //metoda łącząca zwracająca obiekt
    @Provides
    fun providesScoreDao(highScoreDatabase: HighScoreDatabase): ScoreDao {
        return highScoreDatabase.scoreDao()
    }

    @Provides
    fun providesPlayerScoreDao(highScoreDatabase: HighScoreDatabase): PlayerScoreDao {
        return highScoreDatabase.playerScoreDao()
    }

    @Provides
    fun providesPlayerDao(highScoreDatabase: HighScoreDatabase): PlayerDao {
        return highScoreDatabase.playerDao()
    }

    @Provides
    @Singleton
    fun provideHighScoreDatabase(
        @ApplicationContext applicationContext: Context
    ): HighScoreDatabase {
        return HighScoreDatabase.getDatabase(applicationContext)
    }

}