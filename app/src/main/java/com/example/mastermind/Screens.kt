package com.example.mastermind

import android.net.Uri

sealed class Screen(val route: String) {

    object Login: Screen(route = "first_screen")
    /*
    object First : Screens(route = "first_screen")
    object Second : Screens(route = "second_screen")
    object Third :  Screens(route = "third_screen")
    */
    object Profile: Screen(route = "second_screen/{color_count}") {
        fun passArguments(colorCount: Int): String {
            return "second_screen/$colorCount"
        }
    }
    object Game: Screen(route = "third_screen/{color_count}") {
        fun passArguments(colorCount: Int): String {
            return "third_screen/$colorCount"
        }
    }

    object HighScores: Screen(route = "fourth/{recent_score}/{color_count}") {
        fun passArguments(recentScore: Long?, colorCount: Int?): String {
            return "fourth/$recentScore/$colorCount"
        }
    }

}