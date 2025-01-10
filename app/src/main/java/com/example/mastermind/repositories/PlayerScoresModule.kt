package com.example.mastermind.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PlayerScoresModule {
    @Binds
    abstract fun bindPlayerScoresRepository(playerScoresRepositoryImpl: PlayerScoresRepositoryImpl): PlayerScoresRepository
}