package com.ignatlegostaev.inhabitroutine.feature.search_tasks

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseViewModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksByQueryUseCase
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchTasksViewModel(
    readTasksByQueryUseCase: ReadTasksByQueryUseCase,
    override val viewModelScope: CoroutineScope
) : BaseViewModel<SearchTasksScreenEvent, SearchTasksScreenState, SearchTasksScreenNavigation, SearchTasksScreenConfig>() {

    private val searchQueryState = MutableStateFlow(DEFAULT_QUERY)

    private val allTasksState = searchQueryState
        .debounce(DEBOUNCE_MILLIS)
        .flatMapLatest { searchQuery ->
            if (searchQuery.isNotEmpty()) readTasksByQueryUseCase(searchQuery)
            else flow { emit(emptyList()) }
        }
        .map { allTasks -> allTasks.sortedByDescending { it.createdAt } }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<SearchTasksScreenState> =
        combine(searchQueryState, allTasksState) { searchQuery, allTasks ->
            SearchTasksScreenState(
                searchQuery = searchQuery,
                allTasks = allTasks
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            SearchTasksScreenState(
                searchQuery = searchQueryState.value,
                allTasks = allTasksState.value
            )
        )

    override fun onEvent(event: SearchTasksScreenEvent) {
        when (event) {
            is SearchTasksScreenEvent.OnInputQueryUpdate ->
                onInputQueryUpdate(event)

            is SearchTasksScreenEvent.OnTaskClick ->
                onTaskClick(event)

            is SearchTasksScreenEvent.OnLeaveRequest ->
                onLeaveRequest()
        }
    }

    private fun onInputQueryUpdate(event: SearchTasksScreenEvent.OnInputQueryUpdate) {
        searchQueryState.update { event.value }
    }

    private fun onTaskClick(event: SearchTasksScreenEvent.OnTaskClick) {
        setUpNavigationState(SearchTasksScreenNavigation.EditTask(event.taskId))
    }

    private fun onLeaveRequest() {
        setUpNavigationState(SearchTasksScreenNavigation.Back)
    }

    companion object {
        private const val DEFAULT_QUERY = ""
        private const val DEBOUNCE_MILLIS = 200L
    }

}