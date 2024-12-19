package com.example.fitstate.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.fitstate.data.repository.BodyStateRepository
import com.example.fitstate.ui.model.BodyState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject


sealed interface AddBodyStateAction {
    data class OnSave(
        val weight: Float,
        val note: String?,
        val date : Date
    ) : AddBodyStateAction
}