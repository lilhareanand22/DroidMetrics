package android.ai.droidmetrics.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import android.ai.droidmetrics.profile.data.repository.LocalSkillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class MetricsViewModel @Inject constructor(
    private val repository: LocalSkillRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MetricsUiState(
            targetRole = "Senior Android Engineer",
            yearsOfExperience = "5 years",
            selectedSkills = listOf()
        )
    )
    val uiState = _uiState.asStateFlow()

    fun onTargetRoleChanged(role: String) {
        Timber.d("Target role updated: $role")
        _uiState.update { it.copy(targetRole = role) }
    }

    fun onExperienceChanged(years: String) {
        Timber.d("Experience updated: $years")
        _uiState.update { it.copy(yearsOfExperience = years) }
    }

    fun onSearchQueryChanged(query: String) {
        val suggestions = repository.getSuggestions(query, _uiState.value.selectedSkills)
        _uiState.update {
            it.copy(
                searchQuery = query,
                suggestions = suggestions
            )
        }
    }

    fun onAddSkill(skillName: String) {
        Timber.d("Adding skill: $skillName")
        _uiState.update { state ->
            state.copy(
                selectedSkills = state.selectedSkills + skillName,
                searchQuery = "",
                suggestions = emptyList()
            )
        }
    }

    fun onRemoveSkill(skillName: String) {
        Timber.d("Removing skill: $skillName")
        _uiState.update { state ->
            state.copy(
                selectedSkills = state.selectedSkills.filter { it != skillName }
            )
        }
    }

    fun onValidateClicked() {
        Timber.i("Validation process triggered for profile analysis.")
        _uiState.update { it.copy(isLoading = true) }
    }
}
