package com.example.mastermind.providers

//
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.CreationExtras
//import androidx.lifecycle.viewmodel.initializer
//import androidx.lifecycle.viewmodel.viewModelFactory
//import com.example.mastermind.MasterAndApplication
//import com.example.mastermind.viewModels.GameViewModel
//import com.example.mastermind.viewModels.ProfileViewModel
//import com.example.mastermind.viewModels.ResultsViewModel
//
//object AppViewModelProvider {
//    val Factory = viewModelFactory {
//        initializer {
//            ProfileViewModel(masterAndApplication().container.playersRepository)
//        }
//
//        initializer {
//            GameViewModel(
//                masterAndApplication().container.playersRepository,
//                masterAndApplication().container.scoresRepository
//            )
//        }
//
//        initializer {
//            ResultsViewModel(
//                masterAndApplication().container.playerScoresRepository
//            )
//        }
//    }
//}
//fun CreationExtras.masterAndApplication(): MasterAndApplication =
//    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
//            MasterAndApplication)