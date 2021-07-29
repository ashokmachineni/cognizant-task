package com.example.mvvmdemo.presentation.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdemo.databinding.ItemAlbumBinding

class AlbumAdapter(var activity: Activity, var albums: ArrayList<AlbumModel>) :
    RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, albumModel: AlbumModel) {
//            binding.albumviewmodel = albumModel
            binding.albumTitle.text = albumModel.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, albums[position])
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    fun addAlbumData(arrayList: ArrayList<AlbumModel>) {
//        arrayList.sortBy { it.title }
        albums = arrayList
        notifyDataSetChanged()
    }

    fun sortList() {
        albums.sortBy { it.title }
        notifyDataSetChanged()
    }
}