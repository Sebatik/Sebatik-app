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
import com.bumptech.glide.Glide

class CarouselAdapter(private val images: List<Int>): RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {
    class CarouselViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val carouselImageView: AppCompatImageView = itemView.findViewById(R.id.iv_carousel)

        fun bind(imageUrl: Int) {
            carouselImageView.load(imageUrl) {
                transformations(RoundedCornersTransformation(8f))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(images[position])
    }
}