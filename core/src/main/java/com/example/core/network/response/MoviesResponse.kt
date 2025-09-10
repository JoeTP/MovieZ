package com.example.core.network.response

import com.example.core.network.dto.MovieDto
import com.google.gson.annotations.SerializedName


data class MoviesResponse(
    val page: Long,
    val results: List<MovieDto>,
    @SerializedName("total_pages")
    val totalPages: Long,
    @SerializedName("total_results")
    val totalResults: Long
)

