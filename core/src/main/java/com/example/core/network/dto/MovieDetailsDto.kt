package com.example.core.network.dto

import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(
    val id: Long,
    val adult: Boolean,
    val title: String,
    @SerializedName("genre_ids")
    val genreIDS: List<Long>,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Long
)