package com.example.mastermind

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
    //viewModel: ProfileViewModel = viewModel(factory = AppProvider.Factory),
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
    viewModel2: ResultsViewModel = hiltViewModel<ResultsViewModel>(),
    recentScore: Long?,
    colorCount: Int
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.loadPlayer()
    }

    val name by viewModel.name
    val email by viewModel.email
    val imageUri by viewModel.imageUri

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp), // Leave space for buttons
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Add TopAppBar
                ProfileTopBar()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
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
                        modifier = Modifier
                            .padding(start = 16.dp)
                    ) {
                        Text( text = "Witaj, ${name}!",
                            fontSize = 20.sp,
                            color = Color.Black)
                        Spacer(
                            modifier = Modifier.size(8.dp)
                        )
                        Text(text = "Emial: ${email}",
                            fontSize = 20.sp,
                            color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

//                CircularProgressIndicator(
//                    modifier = Modifier.width(64.dp),
//                    color = Color(0xFF8A2BE2),
//                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                    progress = recentScore?.toFloat()?.div(1) ?: 0.5f
//                )

//                CircularProgressIndicator(//to ogarniecia
//                    modifier = Modifier
//                        .size(100.dp) // Wielkość koła
//                        .padding(horizontal = 16.dp),
//                    color = Color(0xFF8A2BE2),
//                    progress = recentScore?.toFloat()?.div(10) ?: 0.5f// Wartość postępu (od 0.0f do 1.0f)
//                )
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), // Dopasowanie do szerokości ekranu
                    color = Color(0xFF8A2BE2), // Kolor paska postępu
                    trackColor = MaterialTheme.colorScheme.surfaceVariant // Kolor tła paska
                )


            }

            // Buttons at the bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(16.dp),

                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilledButtonExample(onClick = {
                    navController.navigate(route = Screen.Login.route)
                }, text = "Powrót")

                FilledButtonExample(onClick = {
                    navController.navigate(Screen.Game.passArguments(colorCount))
                }, text = "Zagraj")

                FilledButtonExample(onClick = {
                    navController.navigate(Screen.HighScores.passArguments(recentScore = null, colorCount = colorCount))
                }, text = "Wyniki")
            }
        }
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
fun FilledButtonExample(onClick: () -> Unit, text: String) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileCardPreview() {
    ProfileScreen(
        navController = rememberNavController(),
        colorCount = 2,
        recentScore = 2L
    )
}

//@Preview(showBackground = true)
//@Composable
//fun SecondPagePreview() {
//    MastermindTheme {
//        SecondPage(
//            name = "Jan Kowalski",
//            email = "jan.kowalski@example.com",
//            number = "+48123456789",
//            profileImageUri = null,
//            navController = rememberNavController() // Create a NavController for preview
//        )
//
//    }
//}