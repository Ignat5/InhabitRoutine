package com.ignatlegostaev.inhabitroutine.presentation.search_tasks

import androidx.lifecycle.viewModelScope
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksByQueryUseCase
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.SearchTasksViewModel
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenState
import com.ignatlegostaev.inhabitroutine.presentation.base.BaseAndroidViewModel
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