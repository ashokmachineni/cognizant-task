package com.example.mvvmdemo.presentation.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_list")
data class AlbumModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = 0,
    @ColumnInfo(name = "title")
    var title: String? = "",
    @ColumnInfo(name = "userId")
    var userId: Int? = 0
)