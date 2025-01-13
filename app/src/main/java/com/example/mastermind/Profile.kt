package com.example.mastermind

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mastermind.models.ProfileViewModel
import com.example.mastermind.models.ResultsViewModel

private const val TAG = "Profile"

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
    viewModel2: ResultsViewModel = hiltViewModel<ResultsViewModel>(),
    recentScore: Long?,
    colorCount: Int
) {
    val coroutineScope = rememberCoroutineScope()
    val playersFlow = viewModel2.loadPlayerScores()
    var bestScore by remember { mutableStateOf(Long.MAX_VALUE) }
    var averageScore by remember { mutableStateOf(0f) }
    val maxAttempts = 15

    val name by viewModel.name
    val email by viewModel.email
    val imageUri by viewModel.imageUri

    LaunchedEffect(Unit) {
        viewModel.loadPlayer()
        playersFlow.collect { players ->
            val currentPlayerScores = players.filter { it.playerName == name }
            if (currentPlayerScores.isNotEmpty()) {
                bestScore = currentPlayerScores.minOf { it.score }
                averageScore = currentPlayerScores.map { it.score }.average().toFloat()
            } else {
                bestScore = Long.MAX_VALUE
                averageScore = 0f
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileTopBar()

                // Profile Info Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.Gray), CircleShape)
                            .padding(4.dp)
                    ) {
                        if (imageUri != "null") {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.baseline_person_24),
                                contentDescription = "Select profile photo",
                                modifier = Modifier.fillMaxSize(),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Witaj, $name!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Email: $email",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Statistics Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Twoje statystyki",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (bestScore == Long.MAX_VALUE) {
                            NoScoresContent()
                        } else {
                            PlayerScoresContent(
                                bestScore = bestScore,
                                averageScore = averageScore,
                                maxAttempts = maxAttempts
                            )
                        }
                    }
                }
            }

            // Bottom Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavigationButtons(navController, colorCount)
            }
        }
    }
}

@Composable
private fun NoScoresContent() {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "Nie masz jeszcze żadnych wyników",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Text(
            text = "Zagraj swoją pierwszą grę!",
            fontSize = 14.sp,
            color = Color(0xFF8A2BE2),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun PlayerScoresContent(
    bestScore: Long,
    averageScore: Float,
    maxAttempts: Int
) {
    Column {
        // Best Score
        Text(
            text = "Najlepszy wynik: $bestScore prób",
            fontSize = 16.sp,
            color = Color.Black
        )

        LinearProgressIndicator(
            progress = (1f - (bestScore.toFloat() / maxAttempts)).coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .padding(vertical = 8.dp),
            color = Color(0xFF8A2BE2),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Average Score
        Text(
            text = "Średni wynik: ${String.format("%.1f", averageScore)} prób",
            fontSize = 16.sp,
            color = Color.Black
        )

        LinearProgressIndicator(
            progress = (1f - (averageScore / maxAttempts)).coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .padding(vertical = 8.dp),
            color = Color(0xFF8A2BE2),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // Performance Message
        Text(
            text = when {
                bestScore <= 5 -> "Mistrzowski wynik!"
                bestScore <= 8 -> "Świetny wynik!"
                bestScore <= 12 -> "Dobry wynik!"
                else -> "Spróbuj pobić swój rekord!"
            },
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar() {
    TopAppBar(
        title = { Text("Profil") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF8A2BE2),
            titleContentColor = Color.White
        )
    )
}

@Composable
private fun NavigationButtons(
    navController: NavController,
    colorCount: Int
) {
    Button(
        onClick = { navController.navigate(route = Screen.Login.route) },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2)),
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text("Powrót", color = Color.White)
    }

    Button(
        onClick = { navController.navigate(Screen.Game.passArguments(colorCount)) },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2)),
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text("Zagraj", color = Color.White)
    }

    Button(
        onClick = {
            navController.navigate(Screen.HighScores.passArguments(recentScore = null, colorCount = colorCount))
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2)),
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text("Wyniki", color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        navController = rememberNavController(),
        //navController = rememberNavController(),
        colorCount = 5,
        recentScore = null
    )
}