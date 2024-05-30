package com.bangkit.sebatik.ui.home

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.adapter.CarouselAdapter
import com.bangkit.sebatik.data.adapter.ProductAdapter
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.data.models.User
import com.bangkit.sebatik.data.response.ProductResponseItem
import com.bangkit.sebatik.databinding.FragmentHomeBinding
import com.bangkit.sebatik.util.ViewModelFactory
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>
    private val viewModel by viewModels<HomeViewModel>() {
        ViewModelFactory.getInstance(requireContext(), UserPreferences.getInstance(dataStore))
    }
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = requireContext().dataStore
        firebaseAuth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCarousel()
        binding.btnScan.setOnClickListener {
            val options = navOptions {
                anim {
                    enter = R.anim.fade_in
                    popExit = R.anim.fade_out
                }
            }
            findNavController().navigate(R.id.action_homeFragment_to_scanFragment, null, options)
        }

        viewModel.getProducts().observe(requireActivity()) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        productList(it.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Log.d("HomeFragment", "onViewCreated: ${it.error}")
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.username.observe(viewLifecycleOwner) { username ->
            binding.tvUsername.text = "Hello ${username}"
        }

        viewModel.fetchUsername()
    }

    private fun setupCarousel() {
        CarouselSnapHelper().attachToRecyclerView(binding.rvExplore)
        binding.rvExplore.adapter = CarouselAdapter(images = getImages())
    }

    private fun getImages(): List<Int> {
        return listOf(
            R.drawable.carousel_1,
            R.drawable.carousel_2,
            R.drawable.carousel_3
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun productList(items: List<ProductResponseItem>) {
        val adapter = ProductAdapter()
        adapter.submitList(items)
        binding.rvLatestProduct.adapter = adapter
        binding.rvLatestProduct.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

//    override fun onResume() {
//        super.onResume()
//        val username: String? = null
//        val isDataFetched = false
//        if (!isDataFetched) {
//            fetchUsername()
//        } else {
//            binding.tvUsername.text = username
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}