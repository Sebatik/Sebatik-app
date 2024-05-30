package com.bangkit.sebatik.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bangkit.sebatik.R
import com.bangkit.sebatik.databinding.ExploreItemBinding
import com.bumptech.glide.Glide

class ExploreAdapter(private val images: List<Int>): RecyclerView.Adapter<ExploreAdapter.ViewHolder>()
{
    class ViewHolder(val binding: ExploreItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ExploreItemBinding = ExploreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ivExplore.setImageResource(images[position])
    }
}