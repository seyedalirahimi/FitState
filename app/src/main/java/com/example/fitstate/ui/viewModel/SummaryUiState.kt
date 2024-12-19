package com.example.fitstate.ui.viewModel

import com.example.fitstate.ui.model.BodyState
import com.example.fitstate.ui.model.Stat

data class SummaryUiState(
    val isLoading: Boolean = true,
    val recentBodyStates: List<BodyState> = emptyList(),
    val bodyStates: List<BodyState> = emptyList(),
    val stats : List<Stat> = emptyList()
)