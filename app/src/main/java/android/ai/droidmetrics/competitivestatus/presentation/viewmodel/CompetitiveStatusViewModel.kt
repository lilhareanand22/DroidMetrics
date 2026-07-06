package android.ai.droidmetrics.competitivestatus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.ai.droidmetrics.competitivestatus.data.repository.CompetitiveStatusRepository
import android.ai.droidmetrics.profile.data.repository.LocalSkillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class CompetitiveStatusViewModel @Inject constructor(
    private val statusRepository: CompetitiveStatusRepository,
    private val localSkillRepository: LocalSkillRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompetitiveStatusUiState())
    val uiState = _uiState.asStateFlow()

    fun loadCompetitiveStatus(role: String, experience: String, skills: List<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = statusRepository.getCompetitiveStatus(role, experience, skills)
            if (result != null) {
                Timber.d("Competitive status loaded: score = ${result.competitiveScore}")
                // Update local skills repository with suggestions
                result.suggestedSkills.forEach { suggestedSkill ->
                    localSkillRepository.addSkillIfNotExist(suggestedSkill)
                }
                _uiState.update {
                    it.copy(
                        competitiveScore = result.competitiveScore,
                        suggestedSkills = result.suggestedSkills,
                        isLoading = false
                    )
                }
            } else {
                Timber.e("Failed to get competitive status from Gemini API")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to calculate competitive status"
                    )
                }
            }
        }
    }
}
