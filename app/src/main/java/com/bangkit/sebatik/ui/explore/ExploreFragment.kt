package com.bangkit.sebatik.ui.explore

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.adapter.ExploreAdapter
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.data.response.DatasItem
import com.bangkit.sebatik.databinding.FragmentExploreBinding
import com.bangkit.sebatik.util.LoadingDialog
import com.bangkit.sebatik.util.ViewModelFactory

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var loadingDialog: LoadingDialog

    private val viewModel: ExploreViewModel by viewModels(){
        ViewModelFactory.getInstance(requireContext(), UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = requireContext().dataStore
        loadingDialog = LoadingDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
//        setupExplore()
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
                    is Result.Loading -> loadingDialog.showLoading()
                    is Result.Success -> {
                        loadingDialog.hideLoading()
                        batikList(it.data)
                    }
                    is Result.Error -> {
                        loadingDialog.hideLoading()
                        binding.tvErrorExplore.visibility = View.VISIBLE
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
        binding.rvExplore.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}