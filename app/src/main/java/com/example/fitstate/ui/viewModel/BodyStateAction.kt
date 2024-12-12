package com.example.fitstate.ui.viewModel

import com.example.fitstate.ui.model.BodyState


sealed interface BodyStateAction {
    data class OnAddBodyState(val bodyState: BodyState) : BodyStateAction
    data object OnShowDialog : BodyStateAction
    data object OnCancelDialog : BodyStateAction
}