package com.example.mastermind

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mastermind.entities.PlayerWithScore
import com.example.mastermind.models.ResultsViewModel

@Composable
fun ResultsScreen(
    navController: NavController,
    //viewModel: ResultsViewModel = viewModel(factory = AppProvider.Factory),
    viewModel: ResultsViewModel = hiltViewModel<ResultsViewModel>(),
    recentScore: Long?,
    colorCount: Int?
) {
    val playersFlow = viewModel.loadPlayerScores()
    var playersScore by remember { mutableStateOf(emptyList<PlayerWithScore>()) }
    LaunchedEffect(playersFlow) {
        playersFlow.collect { newPlayer ->
            playersScore = newPlayer
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Top bar with "Results"
        ResultsTopBar()
        Spacer(modifier = Modifier.height(16.dp))
        if (recentScore != null) {
            Text(
                text = "Recent score: $recentScore",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, Color.Gray)
        ) {
            items(playersScore) { playerScore ->
                ScoreRow(playerScore.playerName, playerScore.score.toString())
                HorizontalDivider()
            }
        }

        // Buttons at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    navController.navigate(route = Screen.Login.route)
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))
            ) {
                Text(text = "Wyloguj", color = Color.White)
            }


            Button(
                onClick = {
                    navController.navigate(route = Screen.Game.passArguments(colorCount = colorCount ?: 5))
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))
            ) {
                Text(text = "Nowa gra", color = Color.White)
            }


            Button(
                onClick = {
                    navController.navigate(route = Screen.Profile.passArguments(colorCount = colorCount ?: 5))
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))
            ) {
                Text(text = "Profil", color = Color.White)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsTopBar() {
    TopAppBar(
        title = { Text("Results") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF8A2BE2),
            titleContentColor = Color.White
        )
    )
}

@Composable
fun ScoreRow(name: String, score: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            //.border(rad)
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Normal)
        Text(text = score, fontSize = 18.sp, fontWeight = FontWeight.Normal)
    }
}

@Composable
@Preview(showBackground = true)
fun ResultsScreenPreview() {
    ResultsScreen(
        navController = rememberNavController(),
        recentScore = 2L,
        colorCount = 5
    )
}
