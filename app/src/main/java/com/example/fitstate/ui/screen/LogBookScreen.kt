package com.example.fitstate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitstate.formatToDay
import com.example.fitstate.formatToMonthDay
import com.example.fitstate.ui.model.Log
import com.example.fitstate.ui.viewModel.LogBookUiState
import com.example.fitstate.ui.viewModel.LogBookViewModel
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@Composable
fun LogBookScreenRoot(
    modifier: Modifier = Modifier, viewModel: LogBookViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LogBookScreen(modifier = modifier, uiState = uiState.value)
}


@Composable
private fun LogBookScreen(
    modifier: Modifier = Modifier, uiState: LogBookUiState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(top = 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "LogBook",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.align(Alignment.CenterHorizontally)
        )



        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.monthlyLogs) { monthlyLog ->
                Card(
                    modifier = modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    Column(modifier = modifier.padding(16.dp)) {
                        MonthlyHeader(month = monthlyLog.month)
                        TableHeader(modifier = modifier)
                        monthlyLog.logs.forEach { log ->
                            LogEntryRow(log = log)
                        }
                    }

                }
            }
        }
    }

}

@Composable
fun MonthlyHeader(modifier: Modifier = Modifier, month: String) {
    Text(
        text = month,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun LogEntryRow(modifier: Modifier = Modifier, log: Log) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TableCell(modifier = modifier.weight(1f)) {
            Text(text = log.bodyState.date.formatToDay())
        }

        TableCell(modifier = modifier.weight(1f)) {
            Text(text = log.bodyState.weight.toString())
        }

        TableCell(modifier = modifier.weight(1f)) {
            Text(text = log.movingAverage.roundToInt().toString())
        }

        TableCell(modifier = modifier.weight(1f)) {
            WeeklyRateText(rate = log.weeklyRate)
        }
    }
}


@Composable
fun TableCell(
    modifier: Modifier = Modifier, body: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier.padding(horizontal = 4.dp), contentAlignment = Alignment.Center
    ) {
        body()
    }
}

@Composable
fun TableHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TableCell(
            body = {
                Text(text = "Date", style = MaterialTheme.typography.bodySmall)
            }, modifier = modifier.weight(1f)
        )
        TableCell(
            body = {
                Text(text = "Recorded", style = MaterialTheme.typography.bodySmall)
            }, modifier = modifier.weight(1f)
        )
        TableCell(
            body = {
                Text(text = "Avg", style = MaterialTheme.typography.bodySmall)
            }, modifier = modifier.weight(1f)
        )
        TableCell(
            body = {
                Text(text = "Rate", style = MaterialTheme.typography.bodySmall)
            }, modifier = modifier.weight(1f)
        )
    }
}

@Composable
fun WeeklyRateText(rate: Float?, modifier: Modifier = Modifier) {
    if (rate == null) {
        Text(
            text = "—",
            color = Color.Gray,
            modifier = modifier
        )
        return
    }

    val isPositive = rate > 0
    val cardColor = if (isPositive) Color.Red else Color.Green
    val arrowIcon =
        if (isPositive) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = arrowIcon,
                contentDescription = if (isPositive) "Positive Rate" else "Negative Rate",
                tint = Color.White
            )
            Text(
                text = rate.absoluteValue.roundToInt().toString(),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}


