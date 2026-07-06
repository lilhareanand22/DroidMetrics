package android.ai.droidmetrics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import android.ai.droidmetrics.profile.presentation.view.ProfileSetupScreen
import android.ai.droidmetrics.competitivestatus.presentation.view.CompetitiveStatusScreen
import android.ai.droidmetrics.ui.theme.DroidMetricsTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DroidMetricsTheme {
                var currentScreen by rememberSaveable { mutableStateOf(Screen.ProfileSetup) }
                var profileRole by rememberSaveable { mutableStateOf("") }
                var profileExperience by rememberSaveable { mutableStateOf("") }
                var profileSkills by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
                
                when (currentScreen) {
                    Screen.ProfileSetup -> {
                        ProfileSetupScreen(
                            onValidateClick = { role, experience, skills ->
                                profileRole = role
                                profileExperience = experience
                                profileSkills = skills
                                currentScreen = Screen.CompetitiveStatus
                            }
                        )
                    }
                    Screen.CompetitiveStatus -> {
                        CompetitiveStatusScreen(
                            role = profileRole,
                            experience = profileExperience,
                            skills = profileSkills,
                            onBackClick = {
                                currentScreen = Screen.ProfileSetup
                            }
                        )
                    }
                }
            }
        }
    }
}

enum class Screen {
    ProfileSetup,
    CompetitiveStatus
}