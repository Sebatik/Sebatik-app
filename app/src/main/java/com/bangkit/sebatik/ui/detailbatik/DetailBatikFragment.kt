package com.bangkit.sebatik.ui.detailbatik

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.sebatik.databinding.FragmentDetailBatikBinding
import com.bangkit.sebatik.util.base64ToBitmap
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar

class DetailBatikFragment : Fragment() {

    private var _binding: FragmentDetailBatikBinding? = null
    private val binding get() = _binding!!
    private val args: DetailBatikFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBatikBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolBar : MaterialToolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            tvBatikName.text = args.batikName.replace("_", " ")
            tvDescriptionContent.text = args.batikDesc
            context?.let {
                Glide.with(it.applicationContext)
                    .load(base64ToBitmap(args.batikImg))
                    .into(ivDetailBatik)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}