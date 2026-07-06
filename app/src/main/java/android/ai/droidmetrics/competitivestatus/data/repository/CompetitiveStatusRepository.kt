package android.ai.droidmetrics.competitivestatus.data.repository

import android.ai.droidmetrics.competitivestatus.data.model.CompetitiveStatusResult
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import javax.inject.Inject
import org.json.JSONObject
import timber.log.Timber

class CompetitiveStatusRepository @Inject constructor() {

    private val jsonSchema = Schema.obj(
        properties = mapOf(
            "competitiveScore" to Schema.integer(),
            "suggestedSkills" to Schema.array(Schema.string())
        )
    )

    private val config = generationConfig {
        responseMimeType = "application/json"
        responseSchema = jsonSchema
    }

    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(
        modelName = "gemini-3.5-flash",
        generationConfig = config,
        //requestOptions = RequestOptions(timeoutInMillis = 600.seconds.inWholeMilliseconds)
    )

    suspend fun getCompetitiveStatus(
        role: String,
        experience: String,
        skills: List<String>
    ): CompetitiveStatusResult? {
        Timber.d("Requesting competitive status from Gemini AI for role: $role, experience: $experience")
        val prompt = """
            You are a strict technical recruiter and Android Architect evaluating a candidate's profile. Be highly critical and realistic about today's competitive Android market, which demands strong foundational skills, modern tools, and emerging AI/ML integration capabilities.

            Profile to evaluate:
            - Target Role: $role
            - Years of Experience: $experience
            - Selected Skills: ${skills.joinToString(", ")}

            Evaluation Guidelines:
            1. Scale Alignment:
               - The score must be realistic. If a candidate targets a "Senior" role or has several years of experience (e.g., 5+ years) but has only selected a few skills (e.g., less than 8-10 skills), they are not competitive. Penalize the score heavily.
               - A senior developer must demonstrate competence in core areas: Modern UI (Jetpack Compose), Language (Kotlin), Concurrency (Coroutines/Flow), Dependency Injection (Hilt), Networking (Retrofit/OkHttp), Database (Room), and Architecture (Clean MVVM/MVI).
               - Reward bonus points for today's AI/ML integration skills (Firebase AI Logic, Gemini SDKs, On-device AI).
            2. Scoring Scale:
               - 90-100: Expert senior candidate listing extensive modern skills, testing, profiling, and AI tools.
               - 70-89: Strong developer meeting most standard senior requirements but lacking advanced optimizations or AI tools.
               - 40-69: Mid-level developer, or a Senior profile with a significant lack of core tools (e.g., has 5 years of experience but only lists 3-6 basic skills).
               - 10-39: Junior developer, or a Senior profile listing only 1-3 basic skills.
               - 0-9: No relevant skills listed.

            Be strict. Do not give a high score (like 80%+) to profiles that lack a robust, well-rounded set of modern Android skills for their experience level.

            Return a JSON object matching this schema:
            {
              "competitiveScore": <integer between 0 and 100>,
              "suggestedSkills": [<list of strings representing recommended skills to learn to improve market readiness>]
            }
        """.trimIndent()

        return try {
            val response = model.generateContent(prompt)
            val responseText = response.text
            Timber.d("Gemini Raw Response: $responseText")
            if (responseText != null) {
                val json = JSONObject(responseText)
                val score = json.getInt("competitiveScore")
                val skillsArray = json.getJSONArray("suggestedSkills")
                val suggested = mutableListOf<String>()
                for (i in 0 until skillsArray.length()) {
                    suggested.add(skillsArray.getString(i))
                }
                CompetitiveStatusResult(score, suggested)
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error generating competitive status from Gemini")
            null
        }
    }
}
