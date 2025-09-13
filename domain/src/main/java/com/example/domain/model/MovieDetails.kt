package com.example.domain.model


data class MovieDetails(
    val id: Long,
    val adult: Boolean,
    val backdropPath: String,
    val genres: List<Genre>,
    val homepage: String,
    val imdbID: String,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterUrl: String,
    val releaseYear: String?,
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val title: String,
    val voteAverage: Double,
)