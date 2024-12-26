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

data class LogMyWeightUiState(
    val weight: String = "",
    val notes: String = "",
    val date: Date = Calendar.getInstance().time,
    val weightError: String? = null,
    val isSaved: Boolean = false // To notify the UI of save completion
)

sealed interface AddBodyStateAction {
    object OnSave : AddBodyStateAction
    data class OnWeightChange(val newWeight: String) : AddBodyStateAction
    data class OnNotesChange(val newNotes: String) : AddBodyStateAction
    data class OnDateChange(val dayChange: Int) : AddBodyStateAction
    data class OnDateSet(val newDate: Date) : AddBodyStateAction
}

@HiltViewModel
class AddBodyStateViewModel @Inject constructor(
    private val bodyStateRepository: BodyStateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogMyWeightUiState())
    val uiState = _uiState.asStateFlow()

    private val calendar: Calendar = Calendar.getInstance()

    init {
        viewModelScope.launch {
            loadBodyStateForDate(_uiState.value.date)
        }
    }

    fun onAction(action: AddBodyStateAction) {
        when (action) {
            is AddBodyStateAction.OnSave -> handleSave()
            is AddBodyStateAction.OnWeightChange -> updateWeight(action.newWeight)
            is AddBodyStateAction.OnNotesChange -> updateNotes(action.newNotes)
            is AddBodyStateAction.OnDateChange -> changeDate(action.dayChange)
            is AddBodyStateAction.OnDateSet -> setDate(action.newDate)
        }
    }

    private fun handleSave() {
        val weight = uiState.value.weight.toFloatOrNull()
        if (weight == null || weight <= 0) {
            _uiState.update { it.copy(weightError = "Weight must be a valid number") }
            return
        }

        // Clear error and save the data
        _uiState.update { it.copy(weightError = null) }
        saveBodyState(weight)
    }

    private fun updateWeight(newWeight: String) {
        _uiState.update { it.copy(weight = newWeight) }
    }

    private fun updateNotes(newNotes: String) {
        _uiState.update { it.copy(notes = newNotes) }
    }

    private fun changeDate(dayChange: Int) {
        calendar.time = _uiState.value.date
        calendar.add(Calendar.DAY_OF_MONTH, dayChange)
        loadBodyStateForDate(calendar.time)
    }

    private fun setDate(newDate: Date) {
        calendar.time = newDate
        loadBodyStateForDate(newDate)
    }

    private fun loadBodyStateForDate(date: Date) {
        viewModelScope.launch {
            val bodyState = bodyStateRepository.getBodyState(date)
            _uiState.update {
                it.copy(
                    date = date,
                    weight = bodyState?.weight?.toString() ?: "",
                    notes = bodyState?.notes ?: "",
                    weightError = null, // Clear error when date changes
                    isSaved = false // Reset save state
                )
            }
        }
    }

    private fun saveBodyState(weight: Float) {
        viewModelScope.launch {
            bodyStateRepository.upsert(
                BodyState(
                    weight = weight,
                    notes = uiState.value.notes,
                    date = uiState.value.date
                )
            )
            // Notify UI that save was successful
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}
