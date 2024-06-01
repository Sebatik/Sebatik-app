package com.bangkit.sebatik.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.databinding.AllProductItemBinding
import com.bangkit.sebatik.ui.product.ProductFragment
import com.bangkit.sebatik.ui.product.ProductFragmentDirections
import com.bangkit.sebatik.util.toRupiah
import com.bumptech.glide.Glide

class AllProductAdapter(private val productList : List<Product>): RecyclerView.Adapter<AllProductAdapter.ViewHolder>() {

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
                tvPrice.text = toRupiah(product.price!!)
                tvProductTitle.text = product.batikName
                tvUsername.text = product.username
                tvPhoneNumber.text = product.phoneNumber
                tvDescription.text = product.description
                Glide.with(itemView)
                    .load(product.image)
                    .into(ivAllProduct)
                itemView.setOnClickListener {
                    val action = ProductFragmentDirections.actionProductFragmentToDetailProductFragment(
                        product.username.toString(),
                        product.price.toString(),
                        product.description.toString(),
                        product.batikName.toString(),
                        product.phoneNumber.toString(),
                        product.image.toString()
                    )
                    findNavController(it).navigate(action)
                }
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