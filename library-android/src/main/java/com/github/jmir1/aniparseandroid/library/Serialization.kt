package com.github.jmir1.aniparseandroid.library

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.serializer

@Serializable
data class AniparseResult(
    @SerialName("file_name")
    val fileName: String,
    @SerialName("file_extension")
    val fileExtension: String? = null,
    @SerialName("release_group")
    val releaseGroup: String? = null,
    @SerialName("anime_title")
    val animeTitle: String? = null,
    @Serializable(with=IntListSerializer::class)
    @SerialName("anime_year")
    val animeYear: List<Int>? = null,
    @SerialName("episode_number")
    val episodeNumber: Float? = null,
    @SerialName("episode_number_alt")
    val episodeNumberAlt: Float? = null,
    @SerialName("release_version")
    val releaseVersion: Float? = null,
    @SerialName("episode_title")
    val episodeTitle: String? = null,
    @Serializable(with=StringListSerializer::class)
    @SerialName("video_resolution")
    val videoResolution: List<String>? = null,
    @Serializable(with=StringListSerializer::class)
    @SerialName("video_term")
    val videoTerm: List<String>? = null,
    @Serializable(with=StringListSerializer::class)
    @SerialName("audio_term")
    val audioTerm: List<String>? = null,
    @SerialName("file_checksum")
    val fileChecksum: String? = null,
    @SerialName("episode_prefix")
    val episodePrefix: String? = null,
    @SerialName("anime_season_prefix")
    val animeSeasonPrefix: String? = null,
    @Serializable(with=StringListSerializer::class)
    @SerialName("other")
    val other: List<String>? = null,
)

// If the object is not an array, then it is a single object that should be wrapped into the array
object StringListSerializer :
    JsonTransformingSerializer<List<String>>(serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element !is JsonArray) JsonArray(listOf(element)) else element
}

object IntListSerializer :
    JsonTransformingSerializer<List<Int>>(serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element !is JsonArray) JsonArray(listOf(element)) else element
}
