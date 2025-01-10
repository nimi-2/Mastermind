package com.example.mastermind

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mastermind.models.ProfileViewModel
import com.example.mastermind.ui.theme.MastermindTheme
//import com.example.mastermind.ui.theme.MastermindTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            MastermindTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    ProfileScreen()
//                }
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }

        }

    }
}


@Composable
fun Profile(
    navController: NavController,
//    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>()
) {


    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var colors by rememberSaveable { mutableStateOf("") }

    val isNameValid = name.isNotBlank()
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isColorsValid = colors.toIntOrNull()?.let { it in 5..10 } ?: false

    val coroutineScope = rememberCoroutineScope()

    val infiniteScaleAnimation = rememberInfiniteTransition(label = "infiniteScaleAnimation")
    val titleScale by infiniteScaleAnimation.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "titleScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Text(
                text = "MasterAnd",
                style = MaterialTheme.typography.displayMedium,
                //horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 24.dp)
                    .graphicsLayer(scaleX = titleScale, scaleY = titleScale),

                )
        //}
        ProfileImageWithPicker(
            imageUri = imageUri,
            //imageUri = imageUri,
            onImagePicked = { imageUri = it }
        )

        OutlinedTextFieldWithError(
            value = name,
            onValueChange = { name = it },
            label = "Enter name",
            isError = !isNameValid,
            errorMessage = "Name cannot be empty",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        OutlinedTextFieldWithError(
            value = email,
            onValueChange = { email = it },
            label = "Enter email",
            isError = email.isNotBlank() && !isEmailValid,
            errorMessage = "Enter valid email address",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextFieldWithError(
            value = colors,
            onValueChange = { colors = it },
            label = "Enter number of colors",
            isError = colors.isNotBlank() && !isColorsValid,
            errorMessage = "Number must be between 5 and 10",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                viewModel.email.value = email
                viewModel.name.value = name
                viewModel.imageUri.value = imageUri.toString()

                coroutineScope.launch {
                    viewModel.savePlayer()
                    navController.navigate(
                        route = Screen.Profile.passArguments(colors.toInt())
                    )
                }

            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isNameValid && isEmailValid && isColorsValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFA446DC)
                // Replace with desired purple hex code
                // Replace with a purple shade if needed
            )
        ) {
            Text("Next")
        }
    }
}

@Composable
fun OutlinedTextFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            trailingIcon = {
                if (isError) {
                    Icon(Icons.Default.Warning, "error", tint = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun ProfileImageWithPicker(
    imageUri: Uri?,
    onImagePicked: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImagePicked(it) }
    }
    Box(
        modifier = modifier
            .size(150.dp)
            .clickable { imagePickerLauncher.launch("image/*") },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(150.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.baseline_question_mark_24),

                )
        } else {
            Icon(
                painter = painterResource(R.drawable.baseline_question_mark_24),
                contentDescription = "Select profile photo",
                modifier = Modifier.size(150.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
/*

@Composable
fun SecondPage(
    name: String,
    email: String,
    number: String,
    profileImageUri: Uri?,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome, $name!")
        Text(text = "Email: $email")
        Text(text = "Phone Number: $number")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("thirdPage") }) {
            Text("Go to Third Page")
        }
    }
}

@Composable
fun MasterAndGameScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to the Game Screen!", style = MaterialTheme.typography.headlineMedium)
        // Możesz tu dodać elementy interfejsu gry, np. przyciski, liczby, etc.
        Button(
            onClick = { /* Logika gry */ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Start Game")
        }
    }
}
 */
//@Preview
//@Composable
//fun ProfileScreenPreview() {
//    MasterAndTheme {
//        ProfileScreen()
//    }
//}