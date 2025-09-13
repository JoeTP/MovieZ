package com.example.data.mapper

import com.example.core.network.dto.GenreDto
import com.example.core.network.dto.MovieDetailsDto
import com.example.core.network.dto.SpokenLanguageDto
import com.example.core.utils.constants.AppStrings.Companion.IMAGE_URL
import com.example.domain.model.Genre
import com.example.domain.model.MovieDetails
import com.example.domain.model.SpokenLanguage

fun MovieDetailsDto.toDomain(): MovieDetails = MovieDetails(
    id = id,
    title = title,
    releaseYear = releaseDate.split("-").first(),
    posterUrl = IMAGE_URL + posterPath,
    adult = adult,
    backdropPath = backdropPath,
    genres = genres.map { it.toDomain() },
    homepage = homepage,
    imdbID = imdbID,
    originCountry = originCountry,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    spokenLanguages = spokenLanguages.map { it.toDomain() },
    status = status,
    tagline = tagline,
    voteAverage = "%.1f".format(voteAverage).toDouble(),
)

fun GenreDto.toDomain() = Genre(
    id = id,
    name = name,
)

fun SpokenLanguageDto.toDomain() = SpokenLanguage(
    englishName = englishName,
    iso639_1 = iso639_1,
    name = name,
)
