package com.example.fitstate.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstate.data.repository.BodyStateRepository
import com.example.fitstate.ui.model.BodyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddBodyStateViewModel @Inject constructor(
    private val bodyStateRepository: BodyStateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogMyWeightUiState())
    val uiState = _uiState.asStateFlow()

    fun onWeightChange(newWeight: String) {
        _uiState.update { it.copy(weight = newWeight) }
    }

    fun onNotesChange(newNotes: String) {
        _uiState.update { it.copy(notes = newNotes) }
    }

    fun onDateChange(newDate: Date) {
        _uiState.update { it.copy(date = newDate) }
    }


    fun onAction(action: AddBodyStateAction, onDismiss: () -> Unit) {
        when (action) {
            is AddBodyStateAction.OnSave ->
                if (uiState.value.weight.isEmpty() || uiState.value.weight.toFloatOrNull() == null) {
                    _uiState.update {
                        it.copy(weightError = "Weight is required")
                    }
                } else {
                    onSave()
                    onDismiss()
                }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            bodyStateRepository.upsert(
                BodyState(
                    uiState.value.weight.toFloat(),
                    uiState.value.notes,
                    uiState.value.date
                )
            )
        }
    }
}


data class LogMyWeightUiState(
    val weight: String = "",
    val notes: String = "",
    val date: Date = Calendar.getInstance().time,
    val weightError: String? = null
)


