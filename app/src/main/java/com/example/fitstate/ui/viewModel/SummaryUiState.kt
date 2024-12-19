package com.example.fitstate.ui.viewModel

import com.example.fitstate.ui.model.BodyState
import com.example.fitstate.ui.model.Stat
import java.lang.Error

data class SummaryUiState(
    val isLoading: Boolean = true,
    val recentBodyStates: List<BodyState> = emptyList(),
    val bodyStates: List<BodyState> = emptyList(),
    val stats: List<Stat> = emptyList(),
    val errorMessage: String? = null
)