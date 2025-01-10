package com.example.mastermind

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SetupNavGraph(navController: NavHostController) {

    val transitionDuration = 1000
    val enterTransitionEasing = EaseIn
    val exitTransitionEasing = EaseOut

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.Login.route ,
                    enterTransition = {
                fadeIn() + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(transitionDuration, easing = enterTransitionEasing)
                )
            },
            exitTransition = {
                fadeOut() + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(transitionDuration, easing = exitTransitionEasing)
                )
            }
        ) {
            Profile(navController = navController)
        }

        composable(
            route = Screen.Profile.route,
            arguments = listOf(
                navArgument("color_count") { type = NavType.IntType }
            ),
            enterTransition = {
                fadeIn() + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(transitionDuration, easing = enterTransitionEasing)
                )
            },
            exitTransition = {
                fadeOut() + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(transitionDuration, easing = exitTransitionEasing)
                )
            }
        ) {backStackEntry ->
            val colorCount = backStackEntry.arguments?.getInt("color_count")
            val recentScore = backStackEntry.arguments?.getString("recent_score")?.toLongOrNull()
            ProfileScreen(navController = navController, colorCount = colorCount!!, recentScore = recentScore)
        }

        composable(
            route = Screen.Game.route,
            arguments = listOf(
                navArgument("color_count") { type = NavType.IntType }
            ),
            enterTransition = {
                fadeIn() + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(transitionDuration, easing = enterTransitionEasing)
                )
            },
            exitTransition = {
                fadeOut() + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(transitionDuration, easing = exitTransitionEasing)
                )
            }
        ) { backStackEntry ->
            val colorCount = backStackEntry.arguments?.getInt("color_count")
            // TODO(change to default value rather then crash)
            GameScreen(
                navController = navController,
                colorCount = colorCount ?: throw IllegalStateException("no color count passed")
            )
        }

        composable(
            route = Screen.HighScores.route,
            arguments = listOf(
                navArgument("recent_score") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("color_count") {
                    type = NavType.StringType
                    defaultValue = "5"
                }
            ),
            enterTransition = {
                fadeIn() + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(transitionDuration, easing = enterTransitionEasing)
                )
            },
            exitTransition = {
                fadeOut() + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(transitionDuration, easing = exitTransitionEasing)
                )
            }
        ) { backStackEntry ->
            val recentScore = backStackEntry.arguments?.getString("recent_score")?.toLongOrNull()
            val colorCount = backStackEntry.arguments?.getString("color_count")?.toIntOrNull()
            ResultsScreen(navController = navController, recentScore = recentScore, colorCount = colorCount)
        }
    }
}