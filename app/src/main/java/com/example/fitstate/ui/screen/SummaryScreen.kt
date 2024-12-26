package com.example.fitstate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitstate.ui.model.BodyState
import com.example.fitstate.ui.model.Stat
import com.example.fitstate.ui.viewModel.SummaryUiState
import com.example.fitstate.ui.viewModel.SummaryViewModel
import java.util.*

@Composable
fun SummaryScreenRoot(
    modifier: Modifier = Modifier, viewModel: SummaryViewModel
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    SummaryScreen(state = state.value, modifier = modifier)
}

@Composable
fun SummaryScreen(
    state: SummaryUiState, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Summary",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.align(Alignment.CenterHorizontally)
        )

        Card(
            modifier = modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Recent Stats",
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleMedium
                )
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    WeightChart(bodyStates = state.recentBodyStates)
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier.fillMaxWidth()
                ) {
                    items(state.stats) { item ->
                        StateItem(name = item.name, value = item.value)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Weight Chart", style = MaterialTheme.typography.titleMedium
                )
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    WeightChart(
                        modifier = modifier, bodyStates = state.bodyStates
                    )
                }
            }
        }
    }
}

@Composable
fun StateItem(
    name: String, value: Float, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


// Previews
@Preview(showBackground = true)
@Composable
private fun SummaryScreenPreview() {
    SummaryScreen(
        state = SummaryUiState(
            isLoading = false,
            recentBodyStates = listOf(
                BodyState(weight = 70.5f, date = Date()),
                BodyState(weight = 71.0f, date = Date())
            ),
            bodyStates = listOf(
                BodyState(weight = 69.5f, date = Date()),
                BodyState(weight = 72.0f, date = Date())
            ),
            stats = listOf(
                Stat(name = "BMI", value = 22.5f),
                Stat(name = "Goal", value = 70f)
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun StateItemPreview() {
    StateItem(name = "BMI", value = 22.5f)
}

@Preview(showBackground = true)
@Composable
private fun WeightChartPreview() {
    WeightChart(
        bodyStates = listOf(
            BodyState(weight = 70.5f, date = Date()),
            BodyState(weight = 71.0f, date = Date())
        )
    )
}
