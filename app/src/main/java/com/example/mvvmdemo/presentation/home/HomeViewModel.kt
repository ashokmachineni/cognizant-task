package com.example.mvvmdemo.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemo.domain.network.ApiServices
import com.example.mvvmdemo.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(private val apiServices: ApiServices) : BaseViewModel() {

    val albumData: MutableLiveData<ArrayList<AlbumModel>> = MutableLiveData()

    fun getAlbum() {
        launch {
            kotlin.runCatching {
                apiServices.gelAlbum()
            }.fold({
                albumData.postValue(it)
            }, {
                exceptionLiveData.postValue(it)
            })
        }
    }

    fun getDetail(): LiveData<ArrayList<AlbumModel>> {
        return albumData
    }
}