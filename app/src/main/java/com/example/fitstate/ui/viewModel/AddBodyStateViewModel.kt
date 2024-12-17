package com.example.fitstate.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitstate.data.repository.BodyStateRepository
import com.example.fitstate.ui.model.BodyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class AddBodyStateViewModel @Inject constructor(
    private val bodyStateRepository: BodyStateRepository
) : ViewModel() {


    fun onAction(action: AddBodyStateAction) {
        when (action) {
            is AddBodyStateAction.OnSave ->
                viewModelScope.launch {
                    bodyStateRepository.upsert(
                        BodyState(
                            action.weight,
                            action.note,
                            Clock.System.now()
                        )
                    )
                }
        }
    }
}