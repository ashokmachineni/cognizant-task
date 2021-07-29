package com.example.mvvmdemo.domain.network

import com.example.mvvmdemo.presentation.home.AlbumModel
import com.example.mvvmdemo.utility.ApiConstant
import retrofit2.http.GET


interface ApiServices {
    @GET(ApiConstant.albums)
    suspend fun gelAlbum(): ArrayList<AlbumModel>
}