package com.example.fitstate.ui.viewModel

import com.example.fitstate.ui.model.BodyState

data class BodyStateUiState(
    val isLoading: Boolean = true,
    val isShowDialog: Boolean = false,
    val bodyStates: List<BodyState> = emptyList(),
    val error: String? = null,

    )
