package com.example.core.network.dto

import com.google.gson.annotations.SerializedName


data class MovieDetailsDto (
    val id: Long,
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val genres: List<GenreDto>,
    val homepage: String,
    @SerializedName("imdb_id")
    val imdbID: String,
    @SerializedName("origin_country")
    val originCountry: List<String>,
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
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguageDto>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double
)

data class GenreDto (
    val id: Long,
    val name: String
)

data class SpokenLanguageDto (
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_639_1")
    val iso639_1: String,
    val name: String
)
