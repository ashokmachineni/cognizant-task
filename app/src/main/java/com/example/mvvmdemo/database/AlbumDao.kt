package com.example.mvvmdemo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmdemo.presentation.home.AlbumModel

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlbum(albumModel: AlbumModel)

    @Query("SELECT * FROM album_list")
    fun getAlbumList(): List<AlbumModel>
}