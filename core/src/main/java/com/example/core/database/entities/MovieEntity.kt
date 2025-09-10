package com.example.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.utils.constants.AppStrings.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String?,
    val releaseYear: String?,
    val posterPath : String?
)