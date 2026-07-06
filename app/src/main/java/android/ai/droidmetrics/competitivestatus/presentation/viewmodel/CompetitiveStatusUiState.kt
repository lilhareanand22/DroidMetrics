package android.ai.droidmetrics.competitivestatus.presentation.viewmodel

data class CompetitiveStatusUiState(
    val competitiveScore: Int = 0,
    val suggestedSkills: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
