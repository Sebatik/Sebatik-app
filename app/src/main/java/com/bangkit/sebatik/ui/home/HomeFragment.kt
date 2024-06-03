package com.bangkit.sebatik.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.adapter.CarouselAdapter
import com.bangkit.sebatik.data.adapter.ProductAdapter
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.databinding.FragmentHomeBinding
import com.bangkit.sebatik.util.ViewModelFactory
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var  firebaseRef : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private val viewModel by viewModels<HomeViewModel>() {
        ViewModelFactory.getInstance(requireContext(), UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = requireContext().dataStore
        firebaseAuth = Firebase.auth
        firebaseRef = FirebaseDatabase.getInstance().getReference("products")
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
        setupHomepage()

        binding.apply {
            btnScan.setOnClickListener {
                navigateToDestination(R.id.action_homeFragment_to_scanFragment, it)
            }
        }
    }

    private fun setupHomepage() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            showLoading(loading)
        }
        viewModel.product.observe(viewLifecycleOwner) { product ->
            setProduct(product)
        }
        viewModel.username.observe(viewLifecycleOwner) { username ->
            binding.tvUsername.text = "Hello ${username}"
        }
        viewModel.fetchUsername()
        viewModel.loadProducts()
    }

    private fun setProduct(product: List<Product>) {
        val productAdapter = ProductAdapter()
        productAdapter.submitList(product)
        binding.rvLatestProduct.adapter = productAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvLatestProduct.layoutManager = layoutManager
    }

    private fun navigateToDestination(destinationId: Int, anchorView: View) {
        anchorView.postDelayed({
            val options = navOptions {
                anim {
                    enter = R.anim.fade_in
                    exit = R.anim.fade_out
                    popEnter = R.anim.fade_in
                    popExit = R.anim.fade_out
                }
            }
            findNavController().navigate(destinationId, null, options)
        }, 500)
    }

    private fun setupCarousel() {
        CarouselSnapHelper().attachToRecyclerView(binding.rvExplore)
        binding.rvExplore.adapter = CarouselAdapter(getImages())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}