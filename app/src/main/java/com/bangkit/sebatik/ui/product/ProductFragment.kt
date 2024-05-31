package com.bangkit.sebatik.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.adapter.AllProductAdapter
import com.bangkit.sebatik.data.adapter.ProductAdapter
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.data.response.ProductResponseItem
import com.bangkit.sebatik.databinding.FragmentProductBinding
import com.bangkit.sebatik.util.ViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var productList: ArrayList<Product>
    private lateinit var  firebaseRef : DatabaseReference

    private val viewModel: ProductViewModel by viewModels(){
        ViewModelFactory.getInstance(requireContext(), UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = requireContext().dataStore
        firebaseRef = FirebaseDatabase.getInstance().getReference("products")
        productList = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        loadAllProducts()
        loadProducts()
        binding.btnPost.setOnClickListener { addProduct() }
        binding.rvProduct.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }


    }

    private fun addProduct() {
        binding.btnPost.postDelayed({
            val options = navOptions {
                anim {
                    enter = R.anim.fade_in
                    exit = R.anim.fade_out
                    popEnter = R.anim.fade_in
                    popExit = R.anim.fade_out
                }
            }
            findNavController().navigate(R.id.action_productFragment_to_addProductFragment, null, options)
        }, 500)
    }

//    private fun loadAllProducts() {
//        viewModel.getAllProducts().observe(requireActivity()) {
//            if (it != null) {
//                when (it) {
//                    is Result.Loading -> showLoading(true)
//                    is Result.Success -> {
//                        showLoading(false)
//                        allProductList(it.data)
//                    }
//                    is Result.Error -> {
//                        showLoading(false)
//                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }

//    private fun allProductList(product: PagingData<ProductResponseItem>) {
//        val adapter = AllProductAdapter()
//        adapter.submitData(lifecycle, product)
//        binding.rvProduct.adapter = adapter
//        binding.rvProduct.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
//    }

    private fun loadProducts() {
        showLoading(true)
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        showLoading(false)
                        val product = productSnapshot.getValue(Product::class.java)
                        productList.add(product!!)
                    }
                    val adapter = AllProductAdapter(productList)
                    binding.rvProduct.adapter = adapter
                    productList.reverse()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                showLoading(false)
                Toast.makeText(context, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}