package com.example.fitstate.ui.screen

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(date)
}

@Composable
fun LogMyWeightDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onAddClicked: (Float, String?, Date) -> Unit
) {
    val calendar = remember { Calendar.getInstance() }
    var selectedDate by remember { mutableStateOf(formatDate(calendar.time)) }
    var weight by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(onDismissRequest = onDismissRequest, title = {
        Text(
            text = "Log my weight", style = MaterialTheme.typography.titleLarge
        )
    }, text = {
        Column(
            modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    calendar.add(Calendar.DAY_OF_MONTH, -1)
                    selectedDate = formatDate(calendar.time)
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Date")
                }
                TextButton(onClick = {
                    showDatePicker(context, calendar) { updatedDate ->
                        calendar.time = updatedDate
                        selectedDate = formatDate(calendar.time)
                    }
                }) {
                    Text(
                        text = selectedDate,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                IconButton(onClick = {
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    selectedDate = formatDate(calendar.time)
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Date")
                }
            }
            // Weight TextField
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight") },
                modifier = modifier.fillMaxWidth()
            )

            // Notes TextField
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = modifier.fillMaxWidth()
            )
        }
    }, confirmButton = {
        Button(onClick = {
            onAddClicked(weight.toFloat(), notes, calendar.time)
            onDismissRequest()
        }) {
            Text(text = "Add")
        }
    }, dismissButton = {
        TextButton(onClick = onDismissRequest) {
            Text(text = "Cancel")
        }
    })
}

fun showDatePicker(context: Context, calendar: Calendar, onDateSelected: (Date) -> Unit) {
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            onDateSelected(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

@Preview(showBackground = true)
@Composable
fun LogMyWeightDialogPreview(modifier: Modifier = Modifier) {
    LogMyWeightDialog(onDismissRequest = {
        println("OnSave")
    }, onAddClicked = { _, _ ,_->
        println("OnDelete")
    })
}