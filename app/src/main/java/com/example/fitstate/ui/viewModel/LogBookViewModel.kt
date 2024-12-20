package com.example.fitstate.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstate.data.repository.BodyStateRepository
import com.example.fitstate.ui.model.Log
import com.example.fitstate.ui.model.MonthlyLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LogBookViewModel @Inject constructor(
    private val bodyStateRepository: BodyStateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogBookUiState())
    val uiState = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            bodyStateRepository.getBodyStates().collect { bodyStates ->


                val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                val sortedBodyStates = bodyStates.sortedBy { it.date }
                val logs = bodyStates.sortedBy { it.date }.mapIndexed { index, bodyState ->
                    val movingAverage =
                        sortedBodyStates.take(index + 1).map { it.weight }.average().toFloat()
                    val weeklyRate = if (index > 0) {
                        val previousWeight = sortedBodyStates[index - 1].weight
                        bodyState.weight - previousWeight
                    } else null
                    Log(bodyState = bodyState, movingAverage = movingAverage, weeklyRate = weeklyRate)
                }

                val sortedLogs = logs.sortedByDescending { it.bodyState.date }
                val groupedByMonth = sortedLogs.groupBy { dateFormat.format(it.bodyState.date) }
                val monthlyLogs = groupedByMonth.map { (month, states) ->
                        MonthlyLog(
                            month = month,
                            logs = states
                        )
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        monthlyLogs = monthlyLogs
                    )
                }
            }
        }

    }
}


data class LogBookUiState(
    val isLoading: Boolean = false,
    val monthlyLogs: List<MonthlyLog> = emptyList(),
    val errorMessage: String? = null

)