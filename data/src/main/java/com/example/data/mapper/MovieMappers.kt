package com.example.data.mapper

import com.example.core.database.entities.MovieEntity
import com.example.core.network.dto.MovieDto
import com.example.core.utils.constants.AppStrings.Companion.IMAGE_URL
import com.example.domain.model.Movie

fun MovieDto.toEntity() = MovieEntity(
    id = id,
    title = title,
    releaseYear = releaseDate?.split("-")?.first(),
    posterPath = posterPath
)

fun MovieEntity.toDomain() = Movie(
    id = id,
    title = title,
    releaseYear = releaseYear?.split("-")?.first(),
    posterUrl = IMAGE_URL + posterPath,
)