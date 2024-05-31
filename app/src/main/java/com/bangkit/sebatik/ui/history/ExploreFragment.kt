package com.bangkit.sebatik.ui.history

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.adapter.ExploreAdapter
import com.bangkit.sebatik.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupExplore()
    }

    private fun setupExplore() {
        binding.apply {
            rvExplore.adapter = ExploreAdapter(getImages())
            rvExplore.setHasFixedSize(true)
            binding.rvExplore.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }

    private fun getImages(): List<Int> {
        return listOf(
            R.drawable.explore_1,
            R.drawable.explore_2,
            R.drawable.explore_3,
            R.drawable.explore_4,
            R.drawable.explore_5,
            R.drawable.explore_6,
            R.drawable.explore_7,
            R.drawable.explore_8,
            R.drawable.explore_9,
            R.drawable.explore_10
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}