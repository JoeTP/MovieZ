package com.example.data.mapper

import com.example.core.database.entities.MovieEntity
import com.example.core.network.response.MovieDto
import com.example.domain.model.Movie

fun MovieDto.toEntity() = MovieEntity(

)

fun MovieEntity.toDomain() = Movie(

)