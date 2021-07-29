package com.example.mvvmdemo.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mvvmdemo.presentation.home.AlbumModel
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MvvmDatabaseTest : TestCase() {

    private lateinit var db: MvvmDatabase
    private lateinit var dao: AlbumDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MvvmDatabase::class.java).build()
        dao = db.albumDao()
    }

    @After
    fun closeDb() {
        db.close()
    }


    @Test
    fun writeAndReadSpend() {
        val album = AlbumModel(1, "kghsl", 1)
        dao.insertAlbum(album)
        val albums = dao.getAlbumList()
        assertThat(albums.contains(album)).isTrue()
    }

}