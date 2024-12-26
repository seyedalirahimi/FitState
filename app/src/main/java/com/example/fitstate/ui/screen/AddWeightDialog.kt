package com.example.fitstate.ui.screen

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitstate.formatToMonthDay
import com.example.fitstate.ui.viewModel.AddBodyStateAction
import com.example.fitstate.ui.viewModel.AddBodyStateViewModel
import com.example.fitstate.ui.viewModel.LogMyWeightUiState
import java.util.Calendar
import java.util.Date

@Composable
fun AddWightDialogRoot(
    modifier: Modifier = Modifier, viewModel: AddBodyStateViewModel, onDismiss: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    AddWightDialog(
        modifier = modifier,
        uiState = uiState.value,
        onDateChange = { dayChange ->
            viewModel.onAction(
                AddBodyStateAction.OnDateChange(dayChange),
            )
        },
        onWeightChange = { weight ->
            viewModel.onAction(
                AddBodyStateAction.OnWeightChange(weight),
            )
        },
        onNotesChange = { notes ->
            viewModel.onAction(
                AddBodyStateAction.OnNotesChange(notes),
            )
        },
        onDismiss = {
            onDismiss()
        },
        onAddClicked = {
            viewModel.onAction(
                AddBodyStateAction.OnSave,
            )
        }
    )
}

@Composable
fun AddWightDialog(
    modifier: Modifier = Modifier,
    uiState: LogMyWeightUiState = LogMyWeightUiState(),
    onDateChange: (Int) -> Unit = {},
    onDateSet: (Date) -> Unit = {},
    onWeightChange: (String) -> Unit = {},
    onNotesChange: (String) -> Unit = {},
    onDismiss: () -> Unit,
    onAddClicked: () -> Unit
) {
    val context = LocalContext.current
    if (uiState.isSaved) {
        onDismiss()
    }

    AlertDialog(onDismissRequest = onDismiss, title = {
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
                    onDateChange(-1)
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Date"
                    )
                }
                TextButton(onClick = {
                    showDatePicker(context, uiState.date) { updatedDate ->
                        onDateSet(updatedDate)
                    }
                }) {
                    Text(
                        text = uiState.date.formatToMonthDay(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                IconButton(onClick = {
                    onDateChange(+1)
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Date"
                    )
                }
            }
            // Weight TextField
            OutlinedTextField(
                value = uiState.weight,
                onValueChange = onWeightChange,
                label = { Text("Weight") },
                isError = uiState.weightError != null,
                supportingText = {
                    if (uiState.weightError != null) {
                        Text(
                            text = uiState.weightError, color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Notes TextField
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = onNotesChange,
                label = { Text("Notes") },
                modifier = modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }
    }, confirmButton = {
        Button(onClick = {
            onAddClicked()
        }) {
            Text(text = "Add")
        }
    }, dismissButton = {
        TextButton(
            onClick = onDismiss
        ) {
            Text(text = "Cancel")
        }
    })
}

fun showDatePicker(context: Context, date: Date, onDateSelected: (Date) -> Unit) {
    val calendar: Calendar = Calendar.getInstance().apply { time = date }
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

// Previews
@Preview(showBackground = true)
@Composable
fun AddWeightDialogPreview(modifier: Modifier = Modifier) {
    AddWightDialog(
        uiState = LogMyWeightUiState(
            date = Date(),
            weight = "70.5",
            notes = "Felt great!",
            isSaved = false
        ),
        onDismiss = { println("Dismiss Clicked") },
        onAddClicked = { println("Add Clicked") }
    )
}


