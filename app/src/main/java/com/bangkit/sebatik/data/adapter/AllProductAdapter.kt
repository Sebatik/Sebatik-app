package com.bangkit.sebatik.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.data.response.ProductResponseItem
import com.bangkit.sebatik.databinding.AllProductItemBinding
import com.bangkit.sebatik.databinding.ProductItemBinding
import com.bumptech.glide.Glide

class AllProductAdapter(private val productList : ArrayList<Product>): RecyclerView.Adapter<AllProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: AllProductItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: AllProductItemBinding = AllProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.apply {
            binding.apply {
                tvPrice.text = product.price
                tvProductTitle.text = product.batikName
                tvUsername.text = product.username
                Glide.with(itemView)
                    .load(product.image)
                    .into(ivAllProduct)
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