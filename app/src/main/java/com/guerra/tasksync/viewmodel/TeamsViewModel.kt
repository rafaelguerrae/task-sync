package com.guerra.tasksync.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guerra.tasksync.data.Team
import com.guerra.tasksync.data.TeamRepository
import com.guerra.tasksync.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {
    private val _teamsData = MutableStateFlow<List<Team>?>(null)
    val teamsData: StateFlow<List<Team>?> = _teamsData.asStateFlow()

    fun getTeams(userId: String) {
        viewModelScope.launch {
            val result = teamRepository.getTeams(userId)
            if (result.isSuccess) {
                _teamsData.value = result.getOrNull()
            } else {
                _teamsData.value = emptyList()
            }
        }
    }
}