package com.example.domain.model

data class MoviesPage(
    val movies: List<Movie>,
    val totalPages: Int,
    val currentPage: Int,
)