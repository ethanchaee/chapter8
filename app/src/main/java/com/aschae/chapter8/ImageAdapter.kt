package com.aschae.chapter8

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aschae.chapter8.databinding.ItemLoadMoreBinding
import com.aschae.chapter8.databinding.ItemPictureBinding

class ImageAdapter(private val itemClickListener: ItemClickListener? = null) : ListAdapter<ImageItems, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ImageItems>() {
        override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return if (viewType == ITEM_IMAGE) {
            val binding = ItemPictureBinding.inflate(layoutInflater, parent, false)
            ImageViewHolder(binding)
        } else {
            val binding = ItemLoadMoreBinding.inflate(layoutInflater, parent, false)
            LoadMoreViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount.dec()) {
            ITEM_LOAD_MORD
        } else {
            ITEM_IMAGE
        }
    }

    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if (originSize == 0) 0 else originSize.inc()
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORD = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bind(currentList[position] as ImageItems.Image)
        } else if (holder is LoadMoreViewHolder) {
            holder.bind(itemClickListener)
        }
    }
}

interface ItemClickListener {
    fun onClickLoadMore()
}

class ImageViewHolder(private val binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ImageItems.Image) {
        binding.previewImageView.setImageURI(item.uri)
    }
}

class LoadMoreViewHolder(private val binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(itemClickListener: ItemClickListener?) {
        binding.root.setOnClickListener {
            itemClickListener?.onClickLoadMore()
        }
    }
}