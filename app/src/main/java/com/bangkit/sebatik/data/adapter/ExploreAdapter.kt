package com.bangkit.sebatik.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sebatik.data.response.ExploreResponseItem
import com.bangkit.sebatik.databinding.BatikItemBinding
import com.bumptech.glide.Glide

class ExploreAdapter(): ListAdapter<ExploreResponseItem, ExploreAdapter.ExploreViewHolder>(DIFF_CALLBACK) {

    class ExploreViewHolder(val binding: BatikItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExploreViewHolder {
        val binding: BatikItemBinding = BatikItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val explore = getItem(position)
        holder.binding.tvExplore.text = explore.title
        Glide.with(holder.itemView)
            .load(explore.image)
            .into(holder.binding.ivExplore)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExploreResponseItem>() {
            override fun areItemsTheSame(
                oldItem: ExploreResponseItem,
                newItem: ExploreResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ExploreResponseItem,
                newItem: ExploreResponseItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


}