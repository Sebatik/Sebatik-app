package com.bangkit.sebatik.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sebatik.data.response.ExploreResponseItem
import com.bangkit.sebatik.databinding.BatikItemBinding
import com.bangkit.sebatik.databinding.ProductItemBinding
import com.bumptech.glide.Glide

class ProductAdapter(): ListAdapter<ExploreResponseItem, ProductAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ProductItemBinding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val explore = getItem(position)
        holder.binding.tvProductTitle.text = explore.title
        holder.binding.tvPrice.text = "$ " + explore.price.toString()
        Glide.with(holder.itemView)
            .load(explore.image)
            .into(holder.binding.ivProduct)
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