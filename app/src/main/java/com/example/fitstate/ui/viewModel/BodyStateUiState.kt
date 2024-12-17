package com.example.fitstate.ui.viewModel

import com.example.fitstate.ui.model.BodyState

data class BodyStateUiState(
    val isLoading: Boolean = true,
    val bodyStates: List<BodyState> = emptyList(),
    val error: String? = null,

    )
