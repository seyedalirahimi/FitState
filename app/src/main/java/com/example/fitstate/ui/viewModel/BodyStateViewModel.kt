package com.example.fitstate.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstate.data.repository.BodyStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BodyStateViewModel @Inject constructor(
    private val bodyStateRepository: BodyStateRepository
) : ViewModel() {

    private var bodyStateCollectionJob: Job? = null

    private val _state = MutableStateFlow(BodyStateUiState())
    val state = _state.asStateFlow()

    init {
        bodyStateCollectionJob = viewModelScope.launch {
            bodyStateRepository.getBodyStates().collect { bodyStates ->
                _state.update { it.copy(bodyStates = bodyStates, isLoading = false) }
            }
        }
    }


    fun onAction(action: BodyStateAction) {
        when (action) {
            is BodyStateAction.OnAddBodyState -> {
                viewModelScope.launch {
                    bodyStateRepository.upsert(action.bodyState)
                }
            }

            is BodyStateAction.OnShowDialog -> {
                _state.update { it.copy(isShowDialog = true) }
            }

            is BodyStateAction.OnCancelDialog -> {
                _state.update { it.copy(isShowDialog = false) }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        bodyStateCollectionJob?.cancel()
    }

}