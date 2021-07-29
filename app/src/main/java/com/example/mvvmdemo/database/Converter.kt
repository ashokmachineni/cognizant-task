package com.example.mvvmdemo.database

import androidx.room.TypeConverter
import com.example.mvvmdemo.presentation.home.AlbumModel
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun listToJson(value: List<AlbumModel>?): String {
        return if (value == null) "" else Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<AlbumModel> {
        if (value.isNotEmpty()) {
            val objects = Gson().fromJson(value, Array<AlbumModel>::class.java) as Array<AlbumModel>
            val list = objects.toList()
            return list
        } else {
            return ArrayList()
        }

    }

}