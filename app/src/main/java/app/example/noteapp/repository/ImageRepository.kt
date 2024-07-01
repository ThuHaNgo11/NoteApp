package app.example.noteapp.repository

import app.example.noteapp.BuildConfig
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.image.ImageURL
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageRepository {

    private val openAI: OpenAI = OpenAI(BuildConfig.imageGenerationApiKey)
    suspend fun makeImageGenerationRequest(prompt: String): String {
        val images = openAI.imageURL( // or openAI.imageJSON
            creation = ImageCreation(
                prompt = prompt,
                model = ModelId("dall-e-2"),
                n = 1,
                size = ImageSize.is256x256
            )
        )
        return images[0].url
    }
}