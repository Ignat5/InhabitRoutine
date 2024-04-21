package com.example.inhabitroutine.presentation.search_tasks

import androidx.lifecycle.viewModelScope
import com.example.inhabitroutine.domain.task.api.use_case.ReadTasksByQueryUseCase
import com.example.inhabitroutine.feature.search_tasks.SearchTasksViewModel
import com.example.inhabitroutine.feature.search_tasks.components.SearchTasksScreenConfig
import com.example.inhabitroutine.feature.search_tasks.components.SearchTasksScreenEvent
import com.example.inhabitroutine.feature.search_tasks.components.SearchTasksScreenNavigation
import com.example.inhabitroutine.feature.search_tasks.components.SearchTasksScreenState
import com.example.inhabitroutine.presentation.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidSearchTasksViewModel @Inject constructor(
    readTasksByQueryUseCase: ReadTasksByQueryUseCase
) : BaseAndroidViewModel<SearchTasksScreenEvent, SearchTasksScreenState, SearchTasksScreenNavigation, SearchTasksScreenConfig>() {

    override val delegateViewModel = SearchTasksViewModel(
        readTasksByQueryUseCase = readTasksByQueryUseCase,
        viewModelScope = viewModelScope
    )

}