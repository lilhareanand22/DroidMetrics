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
            Evaluate the following Android developer profile and return their alignment with the current market.
            Target Role: $role
            Years of Experience: $experience
            Selected Skills: ${skills.joinToString(", ")}

            Return:
            1. An integer 'competitiveScore' between 0 and 100 representing market alignment.
            2. A list of 'suggestedSkills' to improve status.
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
