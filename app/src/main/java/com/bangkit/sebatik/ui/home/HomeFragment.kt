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
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.adapter.AllProductAdapter
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
    private lateinit var productList: ArrayList<Product>
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
        productList = arrayListOf()
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
        fetchUser()
        loadProducts()

        binding.apply {
            rvLatestProduct.setHasFixedSize(true)
            rvLatestProduct.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.btnScan.setOnClickListener {
            val options = navOptions {
                anim {
                    enter = R.anim.fade_in
                    popExit = R.anim.fade_out
                }
            }
            findNavController().navigate(R.id.action_homeFragment_to_scanFragment, null, options)
        }

    }

    private fun fetchUser() {
        viewModel.username.observe(viewLifecycleOwner) { username ->
            binding.tvUsername.text = "Hello ${username}"
        }
        viewModel.fetchUsername()
    }

    private fun loadProducts() {
        showLoading(true)
        firebaseRef.limitToLast(5).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded && !isDetached) {
                    showLoading(false)
                    productList.clear()
                    if (snapshot.exists()) {
                        for (productSnapshot in snapshot.children) {
                            val product = productSnapshot.getValue(Product::class.java)
                            productList.add(product!!)
                        }
                        productList.reverse()
                        val adapter = ProductAdapter(productList)
                        binding.rvLatestProduct.adapter = adapter
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                if (isAdded && !isDetached) {
                    showLoading(false)
                    Toast.makeText(context, "error : ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }

        })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}