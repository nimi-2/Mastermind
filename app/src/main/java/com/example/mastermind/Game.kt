package com.example.mastermind

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mastermind.models.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "Game"

data class RowState(
    val selectedColors: List<Color>,
    val feedbackColors: List<Color>,
    val isClickable: Boolean
)

fun selectNextAvailableColor(
    availableColors: List<Color>,
    selectedColors: List<Color>,
    buttonIndex: Int
): Color {
    val currentColor = selectedColors[buttonIndex]
    val usedColors = selectedColors.filterIndexed { index, _ -> index != buttonIndex }
    val unusedColors = availableColors.filter { it !in usedColors }
    if (unusedColors.isEmpty()) return availableColors.first()

    val currentIndex = unusedColors.indexOf(currentColor)

    return if (currentIndex == -1 || currentIndex == unusedColors.size - 1) {
        unusedColors.first()
    } else {
        unusedColors[currentIndex + 1]
    }
}

fun selectRandomColors(availableColors: List<Color>): List<Color> {
    return availableColors.shuffled().take(4)
}

fun checkColors(
    selectedColors: List<Color>,
    correctColors: List<Color>,
    notFoundColor: Color
): List<Color> {
    val feedbackColors = MutableList(4) { notFoundColor }

    for (i in selectedColors.indices) {
        if (selectedColors[i] == correctColors[i]) {
            feedbackColors[i] = Color.Red
        }
    }

    val remainingSelected = selectedColors.filterIndexed { i, color -> feedbackColors[i] != Color.Red }
    val remainingCorrect = correctColors.filterIndexed { i, color -> feedbackColors[i] != Color.Red }

    for (color in remainingSelected) {
        if (color in remainingCorrect) {
            feedbackColors[selectedColors.indexOf(color)] = Color.Yellow
        }
    }

    return feedbackColors
}

@Composable
fun GameScreen(
    navController: NavController,
    //viewModel: GameViewModel = viewModel(factory = AppProvider.Factory),
    viewModel: GameViewModel = hiltViewModel<GameViewModel>(),
    colorCount: Int
) {
    val allColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta, Color.Gray, Color.Black, Color(0xffff6600),Color(0xfff3c2e2))
    val availableColors = List(colorCount) { index -> allColors[index % allColors.size]}
    val score = remember { mutableIntStateOf(0) }
    var rows = remember {
        mutableStateListOf(
            RowState(
                selectedColors = List(4) { Color.White },
                feedbackColors = List(4) { Color.White },
                isClickable = true
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()

    val correctColors = remember { selectRandomColors(availableColors) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        GameTopBar()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your score: ${score.intValue}",
            fontSize = 30.sp
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(rows.size) { rowIndex ->
                val rowState = rows[rowIndex]
                Spacer(modifier = Modifier.size(25.dp))
                GameRow(
                    selectedColors = rowState.selectedColors,
                    clickable = rowState.isClickable,
                    feedbackColors = rowState.feedbackColors,
                    onSelectColorClick = { index ->
                        if (!rowState.isClickable) {
                            return@GameRow
                        }
                        val newSelectedColors = rowState.selectedColors.toMutableList().apply {
                            this[index] = selectNextAvailableColor(
                                availableColors = availableColors,
                                selectedColors = this,
                                buttonIndex = index
                            )
                        }
                        rows[rowIndex] = rowState.copy(
                            selectedColors = newSelectedColors
                        )
                    },
                    onCheckClick = {
                        if (!rowState.isClickable || rowState.selectedColors.contains(Color.White)) {
                            return@GameRow
                        }

                        val feedback = checkColors(rowState.selectedColors, correctColors, Color.White)
                        rows[rowIndex] = rowState.copy(
                            feedbackColors = feedback,
                            isClickable = false
                        )

                        if (!feedback.all { it == Color.Red }) {
                            rows.add(RowState(
                                selectedColors = List(4) { Color.White },
                                feedbackColors = List(4) { Color.White },
                                isClickable = true
                            ))
                            score.value += 1
                        }

                        if (feedback.all { it == Color.Red }) {
                            viewModel.score.longValue = score.intValue.toLong()
                            coroutineScope.launch {
                                viewModel.savePlayerScore()
                            }

                        }
                    }
                )
            }

        }

        Spacer(modifier = Modifier.weight(0.1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    score.value = 0
                    rows.clear()
                    rows.add(
                        RowState(
                            selectedColors = List(4) { Color.White },
                            feedbackColors = List(4) { Color.White },
                            isClickable = true
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))
            ) {
                Text("Restart")
            }

            Button(
                onClick = {
                    navController.navigate(
                        route = Screen.HighScores
                            .passArguments(recentScore = score.intValue.toLong(), colorCount = colorCount)
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))
            ) {
                Text("Wyniki")
            }
        }
    }
}

@Composable
fun GameRow(
    selectedColors: List<Color>,
    feedbackColors: List<Color>,
    clickable: Boolean,
    onSelectColorClick: (index: Int) -> Unit,
    onCheckClick: () -> Unit
) {
    val isButtonEnabled = clickable && !selectedColors.contains(Color.White)
    val submitButtonVisibleState = remember { MutableTransitionState(false) }
    if (submitButtonVisibleState.targetState != isButtonEnabled)
        submitButtonVisibleState.targetState = isButtonEnabled

    var isRowVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isRowVisible = true
    }

    AnimatedVisibility(
        visible = isRowVisible,
        enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(500)),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            SelectableColorsRow(selectedColors) { index -> onSelectColorClick(index) }
            AnimatedVisibility(
                visibleState = submitButtonVisibleState,
                enter = scaleIn(animationSpec = tween(500)),
                exit = scaleOut(animationSpec = tween(500))
            ) {
                IconButton(
                    onClick = { onCheckClick() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                        .background(color = MaterialTheme.colorScheme.background),
                    colors = IconButtonDefaults.filledIconButtonColors(),
                    enabled = clickable
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "")
                }
            }
            FeedbackCircles(feedbackColors)

        }
    }
}
@Composable
fun FeedbackCircles(colors: List<Color>) {
    val spacedBy = 2.dp

    val colorAnimations = List(4) { remember { Animatable(Color.White) } }
    LaunchedEffect(colors) {
        colors.forEachIndexed { index, color ->
            launch {
                delay(index * 250L)
                colorAnimations[index].animateTo(color, animationSpec = tween(500))
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(bottom = 5.dp)
        ) {
            if (colors.isNotEmpty()) {
                SmallCircle(color = colorAnimations[0].value)
            }
            if (colors.size > 1) {
                SmallCircle(color = colorAnimations[1].value)
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (colors.size > 2) {
                SmallCircle(color = colorAnimations[2].value)
            }
            if (colors.size > 3) {
                SmallCircle(color = colorAnimations[3].value)
            }
        }
    }
}

@Composable
fun SelectableColorsRow(colors: List<Color>, onClick: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        colors.forEachIndexed() { index, color ->
            CheckButton(
                onClick = { onClick(index) },
                color = color
            )
        }
    }
}

@Composable
fun CheckButton(onClick: () -> Unit, color: Color) {
    val colorAnimation = remember { Animatable(color) }
    LaunchedEffect(color) {
        colorAnimation.animateTo(
            targetValue = color,
            animationSpec = repeatable(
                iterations = 3,
                animation = tween(300),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Button(
        onClick = { onClick() },
        modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = colorAnimation.value,
                contentColor = MaterialTheme.colorScheme.onBackground
            )

    ) { }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar() {
    TopAppBar(
        title = { Text("Game") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF8A2BE2),
            titleContentColor = Color.White
        )
    )
}

@Composable
fun SmallCircle(color: Color) {
    val colorAnimation = remember { Animatable(Color.White) }
    LaunchedEffect(color) {
        colorAnimation.animateTo(color, animationSpec = tween(500))
    }
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(colorAnimation.value)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
    )
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    GameScreen(
        navController = rememberNavController(),
        colorCount = 2
    )
}
