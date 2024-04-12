package com.example.inhabitroutine.feature.view_schedule

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val readTaskByIdUseCase: ReadTaskByIdUseCase
) : ViewModel() {



    init {
        Log.d("myTag", "TestViewScheduleViewModel: init...")
        viewModelScope.launch {
            readTaskByIdUseCase.invoke("123").collect {
                Log.d("myTag", "readTaskByIdUseCase: collect: ${it}")
            }
        }
    }

}