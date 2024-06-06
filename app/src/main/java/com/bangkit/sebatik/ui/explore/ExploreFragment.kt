package com.bangkit.sebatik.ui.explore

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.adapter.ExploreAdapter
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.data.response.DatasItem
import com.bangkit.sebatik.databinding.FragmentExploreBinding
import com.bangkit.sebatik.util.ViewModelFactory

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>

    private val viewModel: ExploreViewModel by viewModels(){
        ViewModelFactory.getInstance(requireContext(), UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = requireContext().dataStore

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        setupExplore()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupExplore()
    }

    private fun setupExplore() {
        viewModel.getBatik().observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        batikList(it.data)
                    }
                    is Result.Error -> {
                        showLoading(true)
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun batikList(batikItem: List<DatasItem>) {
        val batikAdapter = ExploreAdapter()
        batikAdapter.submitList(batikItem)
        binding.rvExplore.adapter = batikAdapter
        binding.rvExplore.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}