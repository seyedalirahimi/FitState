package com.example.fitstate.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitstate.ui.model.BodyState
import com.example.fitstate.ui.viewModel.BodyStateAction
import com.example.fitstate.ui.viewModel.BodyStateUiState
import com.example.fitstate.ui.viewModel.BodyStateViewModel
import kotlinx.datetime.Clock

@Composable
fun BodyStateScreenRoot(
    viewModel: BodyStateViewModel
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BodyStateScreen(
        state = state, onAction = viewModel::onAction
    )
}

@Composable
fun BodyStateScreen(
    state: BodyStateUiState, onAction: (BodyStateAction) -> Unit
) {

    Column {
        if (!state.isLoading && state.bodyStates.isNotEmpty()){
            WeightChart(bodyStates = state.bodyStates)
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onAction(BodyStateAction.OnShowDialog) }) {
                Text("Add Body State")
            }
        }

        BodyStateList(bodyStates = state.bodyStates)

    }


    if (state.isShowDialog) {
        AddBodyStateDialog(onDismissRequest = { onAction(BodyStateAction.OnCancelDialog) },
            onSave = { weight, note ->
                onAction(
                    BodyStateAction.OnAddBodyState(
                        BodyState(
                            weight = weight,
                            note = note,
                            timestamp = Clock.System.now()
                        )
                    )
                )
            })
    }

}


@Composable
fun BodyStateList(bodyStates: List<BodyState>) {
    LazyColumn {
        items(bodyStates) { bodyState ->
            BodyStateItem(bodyState)
        }
    }
}

@Composable
fun BodyStateItem(bodyState: BodyState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "${bodyState.weight} kg",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = bodyState.note ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Text(
            modifier = Modifier.padding(8.dp),
            text = bodyState.timestamp.toString(), // Format timestamp as needed
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            textAlign = TextAlign.End
        )
    }
}


@Composable
fun AddBodyStateDialog(
    onDismissRequest: () -> Unit, onSave: (Float, String?) -> Unit
) {
    var weight by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismissRequest, title = { Text("Add Body State") }, text = {
        Column {
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("note") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }
    }, confirmButton = {
        Button(onClick = {
            onSave(
                weight.toFloatOrNull() ?: 0f, note
            )
        }) {
            Text("Save")
        }
    }, dismissButton = {
        Button(onClick = onDismissRequest) {
            Text("Cancel")
        }
    })
}