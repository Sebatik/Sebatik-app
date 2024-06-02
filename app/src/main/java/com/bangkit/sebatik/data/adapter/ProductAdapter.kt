package com.bangkit.sebatik.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.databinding.ProductItemBinding
import com.bangkit.sebatik.util.toRupiah
import com.bumptech.glide.Glide

class ProductAdapter(): ListAdapter<Product, ProductAdapter.ViewHolder>(DIFF_CALLBACK) {

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
        val product = getItem(position)
        holder.apply {
            binding.apply {
                tvPrice.text = toRupiah(product.price!!)
                tvProductTitle.text = product.batikName
                tvUsername.text = product.username
                Glide.with(itemView)
                    .load(product.image)
                    .into(ivProduct)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}