package com.example.fitstate.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstate.data.repository.BodyStateRepository
import com.example.fitstate.ui.model.BodyState
import com.example.fitstate.ui.model.Stat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SummaryUiState(
    val isLoading: Boolean = true,
    val recentBodyStates: List<BodyState> = emptyList(),
    val bodyStates: List<BodyState> = emptyList(),
    val stats: List<Stat> = emptyList(),
    val errorMessage: String? = null
)

sealed interface SummaryAction {}


@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val bodyStateRepository: BodyStateRepository
) : ViewModel() {
    private var bodyStateCollectionJob: Job? = null

    private val _state = MutableStateFlow(SummaryUiState())
    val state = _state.asStateFlow()

    init {
        bodyStateCollectionJob = viewModelScope.launch {
            bodyStateRepository.getBodyStates().catch { e ->
                    _state.update {
                        SummaryUiState(
                            isLoading = false, errorMessage = e.message
                        )
                    }

                }.collect { bodyStates ->
                    if (bodyStates.isEmpty()) {
                        _state.update {
                            SummaryUiState(
                                isLoading = false, errorMessage = "No body states found"
                            )
                        }
                    } else {
                        val high = bodyStates.maxBy { it.weight }.weight
                        val low = bodyStates.minBy { it.weight }.weight
                        val latest = bodyStates.last().weight
                        val trend = latest - low
                        val stats = listOf(
                            Stat("High", high),
                            Stat("Low", low),
                            Stat("Trend", trend),
                            Stat("Latest", latest)
                        )
                        _state.update {
                            it.copy(
                                isLoading = false,
                                recentBodyStates = bodyStates.takeLast(10),
                                bodyStates = bodyStates,
                                stats = stats
                            )
                        }
                    }

                }
        }
    }

    fun onAction(action: SummaryAction) {

    }

    override fun onCleared() {
        super.onCleared()
        bodyStateCollectionJob?.cancel()
    }

}
