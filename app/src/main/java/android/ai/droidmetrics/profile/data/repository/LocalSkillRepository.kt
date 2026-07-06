package android.ai.droidmetrics.profile.data.repository

import android.ai.droidmetrics.profile.data.model.AndroidSkill
import android.ai.droidmetrics.profile.data.model.SkillRepositoryData
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class LocalSkillRepository @Inject constructor() {

    private val allSkills = mutableListOf(
        AndroidSkill("1", "Jetpack Compose"),
        AndroidSkill("2", "Material Design 3 (M3)"),
        AndroidSkill("3", "Views / XML Layouts"),
        AndroidSkill("4", "View Binding / Data Binding"),
        AndroidSkill("5", "Custom Views / Canvas drawing"),
        AndroidSkill("6", "MotionLayout / Core Animations"),
        AndroidSkill("7", "Splash Screen API"),
        AndroidSkill("8", "Jetpack Glance (App Widgets)"),
        AndroidSkill("9", "Kotlin"),
        AndroidSkill("10", "Java"),
        AndroidSkill("11", "Kotlin Coroutines"),
        AndroidSkill("12", "Kotlin Flow (StateFlow / SharedFlow)"),
        AndroidSkill("13", "RxJava"),
        AndroidSkill("14", "Threading / Handlers & Loopers"),
        AndroidSkill("15", "Kotlin Serialization"),
        AndroidSkill("16", "MVVM (Model-View-ViewModel)"),
        AndroidSkill("17", "MVI (Model-View-Intent)"),
        AndroidSkill("18", "Clean Architecture"),
        AndroidSkill("19", "Repository Pattern"),
        AndroidSkill("20", "Jetpack Lifecycle (ViewModel, LiveData)"),
        AndroidSkill("21", "Jetpack Navigation Component"),
        AndroidSkill("22", "Dependency Injection (Dagger Hilt)"),
        AndroidSkill("23", "Dependency Injection (Koin)"),
        AndroidSkill("24", "Android App Modularization"),
        AndroidSkill("25", "Room Database"),
        AndroidSkill("26", "DataStore (Preferences / Proto)"),
        AndroidSkill("27", "SQLite"),
        AndroidSkill("28", "Retrofit"),
        AndroidSkill("29", "Ktor Client"),
        AndroidSkill("30", "OkHttp / Interceptors"),
        AndroidSkill("31", "WorkManager (Background Processing)"),
        AndroidSkill("32", "Paging 3 Library"),
        AndroidSkill("33", "Protocol Buffers (Protobuf)"),
        AndroidSkill("34", "Kotlin Multiplatform (KMP)"),
        AndroidSkill("35", "Compose Multiplatform"),
        AndroidSkill("36", "Wear OS Development"),
        AndroidSkill("37", "Android TV Development"),
        AndroidSkill("38", "Android for Cars (Android Auto / Automotive OS)"),
        AndroidSkill("39", "JUnit 4 / JUnit 5"),
        AndroidSkill("40", "Mockk / Mockito"),
        AndroidSkill("41", "Espresso (UI Testing)"),
        AndroidSkill("42", "UI Automator"),
        AndroidSkill("43", "Baseline Profiles"),
        AndroidSkill("44", "Android Studio Profiler (Memory, CPU, Network)"),
        AndroidSkill("45", "LeakCanary"),
        AndroidSkill("46", "StrictMode / Benchmarking (Macrobenchmark)"),
        AndroidSkill("47", "Firebase AI Logic"),
        AndroidSkill("48", "Gemini Client SDKs"),
        AndroidSkill("49", "Firebase Remote Config"),
        AndroidSkill("50", "Firebase App Check"),
        AndroidSkill("51", "Google Play Integrity API"),
        AndroidSkill("52", "Firebase Cloud Firestore / Realtime Database"),
        AndroidSkill("53", "Firebase Cloud Messaging (FCM Push Notifications)"),
        AndroidSkill("54", "Android Keystore System / Biometric Prompt"),
        AndroidSkill("55", "In-App Updates / In-App Reviews"),
        AndroidSkill("56", "NDK (Native Development Kit) / C++"),
        AndroidSkill("57", "Gradle (Groovy DSL / Kotlin DSL)"),
        AndroidSkill("58", "Gradle Version Catalogs (libs.versions.toml)"),
        AndroidSkill("59", "GitHub Actions / GitLab CI"),
        AndroidSkill("60", "Bitrise"),
        AndroidSkill("61", "Jenkins"),
        AndroidSkill("62", "ProGuard / R8 Obfuscation")
    )

    val masterSkillRepository = SkillRepositoryData(allSkills)

    fun getSuggestions(query: String, excludedSkills: List<String>): List<AndroidSkill> {
        if (query.isBlank()) {
            return emptyList()
        }
        Timber.d("Filtering suggestions for query: $query")
        return allSkills.filter { skill ->
            skill.name.contains(query, ignoreCase = true) && skill.name !in excludedSkills
        }
    }

    fun addSkillIfNotExist(skillName: String) {
        val exists = allSkills.any { it.name.equals(skillName, ignoreCase = true) }
        if (!exists) {
            val newId = (allSkills.size + 1).toString()
            allSkills.add(AndroidSkill(newId, skillName))
            Timber.d("Added new skill to master repository: $skillName")
        }
    }
}
