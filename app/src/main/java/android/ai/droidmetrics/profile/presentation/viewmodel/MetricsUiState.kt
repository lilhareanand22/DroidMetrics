package android.ai.droidmetrics.profile.presentation.viewmodel

import android.ai.droidmetrics.profile.data.model.AndroidSkill

data class MetricsUiState(
    val targetRole: String = "",
    val yearsOfExperience: String = "",
    val searchQuery: String = "",
    val selectedSkills: List<String> = emptyList(),
    val suggestions: List<AndroidSkill> = emptyList(),
    val isLoading: Boolean = false
)
