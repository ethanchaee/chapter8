package com.aschae.chapter8

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aschae.chapter8.databinding.ItemFrameBinding

class FrameAdapter : ListAdapter<ImageItems.Image, FrameViewHolder>(object : DiffUtil.ItemCallback<ImageItems.Image>() {
    override fun areItemsTheSame(oldItem: ImageItems.Image, newItem: ImageItems.Image): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: ImageItems.Image, newItem: ImageItems.Image): Boolean {
        return oldItem == newItem
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemFrameBinding.inflate(layoutInflater, parent, false)
        return FrameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class FrameViewHolder(private val binding: ItemFrameBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ImageItems.Image) {
        binding.previewImageView.setImageURI(item.uri)
    }
}