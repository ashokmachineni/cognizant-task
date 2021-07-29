package com.example.mvvmdemo.presentation.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmdemo.R
import com.example.mvvmdemo.database.AlbumDao
import com.example.mvvmdemo.database.MvvmDatabase
import com.example.mvvmdemo.databinding.ActivityHomeBinding
import com.example.mvvmdemo.presentation.base.BaseActivity
import com.example.mvvmdemo.utility.isNetworkAvailable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : BaseActivity() {

    private var albumDao: AlbumDao? = null
    private var albumAdapter: AlbumAdapter? = null
    private var binding: ActivityHomeBinding? = null
    private val currentViewModel: HomeViewModel by viewModel()
    override fun getBaseViewModel() = currentViewModel
    var albums = ArrayList<AlbumModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val mDatabase = MvvmDatabase.getDatabase(this)
        albumDao = mDatabase.albumDao()
        attachObserver()
        initAdapter()
        if (isNetworkAvailable()) {
            currentViewModel.getAlbum()
        } else {
            setData()
        }
    }

    private fun setData() {
        Thread {
            albums = ArrayList<AlbumModel>(albumDao!!.getAlbumList())
            runOnUiThread {
                albumAdapter!!.addAlbumData(albums)
            }
        }.start()
    }

    private fun attachObserver() {
        currentViewModel.albumData.observe(this) {
//            if (albums.size == 0) {
            albums = it
            albumAdapter!!.addAlbumData(albums)
            Thread {
                for (album in albums) {
                    albumDao!!.insertAlbum(album)
                }
            }.start()
//            }
        }
    }

    private fun initAdapter() {
        albumAdapter = AlbumAdapter(this, albums)
        binding!!.albumRecycler.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = albumAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sortTitle) {
            albumAdapter!!.sortList()
        }
        return true
    }
}