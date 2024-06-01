package com.bangkit.sebatik.ui.detailproduct

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.databinding.FragmentDetailProductBinding
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var productList: ArrayList<Product>
    private lateinit var  firebaseRef : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private val args: DetailProductFragmentArgs by navArgs()

    private val viewModel: DetailProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = Firebase.auth
        firebaseRef = FirebaseDatabase.getInstance().getReference("products")
        productList = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneNumber = "+62"+args.phoneNumber
        binding.apply {
            tvBatikName.text = args.productName
            tvPriceDetailProduct.text = args.price
            tvDescriptionContent.text = args.desc
            tvSellerName.text = args.sellerName
            btnContactSeller.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://wa.me/$phoneNumber")
                startActivity(intent)
            }
            context?.let {
                Glide.with(it.applicationContext)
                    .load(args.image)
                    .into(binding.ivDetailProduct)}
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}