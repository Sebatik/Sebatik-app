package com.bangkit.sebatik.data.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.navOptions
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.response.DatasItem
import com.bangkit.sebatik.databinding.ExploreItemBinding
import com.bangkit.sebatik.ui.explore.ExploreFragmentDirections
import com.bangkit.sebatik.util.base64ToBitmap
import com.bumptech.glide.Glide
import com.pixelcarrot.base64image.Base64Image
import kotlin.io.encoding.ExperimentalEncodingApi

class ExploreAdapter(): ListAdapter<DatasItem, ExploreAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(val binding: ExploreItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val batikItem = getItem(position)
        val convertedImage = base64ToBitmap(batikItem.batikImg)
        holder.binding.apply {
            tvExplore.text = batikItem.batikName.replace("_", " ")
            tvDesc.text = batikItem.batikDesc
            Glide.with(holder.itemView)
                .load(convertedImage)
                .into(ivExplore)
            holder.itemView.setOnClickListener {
                val action = ExploreFragmentDirections.actionExploreFragmentToDetailBatikFragment(
                    batikItem.batikName,
                    batikItem.batikImg,
                    batikItem.batikDesc,
                )
                val options = navOptions {
                    anim {
                        enter = R.anim.fade_in
                        exit = R.anim.fade_out
                        popEnter = R.anim.fade_in
                        popExit = R.anim.fade_out
                    }
                }
                findNavController(it).navigate(action, options)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ExploreItemBinding = ExploreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DatasItem>() {
            override fun areItemsTheSame(oldItem: DatasItem, newItem: DatasItem): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: DatasItem, newItem: DatasItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
