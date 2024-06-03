package com.bangkit.sebatik.ui.addproduct

import com.bangkit.sebatik.util.LoadingDialog
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.databinding.FragmentAddProductBinding
import com.bangkit.sebatik.util.ViewModelFactory
import com.bangkit.sebatik.util.getImageUri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference
    private var username: String? = null
    private var phoneNumber: String? = null
    private var currentImageUri: Uri? = null


    private lateinit var dataStore: DataStore<Preferences>
    private val viewModel by viewModels<AddProductViewModel>() {
        ViewModelFactory.getInstance(requireContext(), UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStore = requireContext().dataStore
        loadingDialog = LoadingDialog(requireContext())

        firebaseRef = FirebaseDatabase.getInstance().getReference("products")
        storageRef = FirebaseStorage.getInstance().getReference("Images")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            btnPost.setOnClickListener { postProduct() }
        }
    }

    private fun postProduct() {
        loadingDialog.showLoading()
        val productName = binding.edName.text.toString().trim()
        val productPrice = binding.edPrice.text.toString().trim()
        val productDescription = binding.edDescription.text.toString().trim()
        viewModel.username.observe(viewLifecycleOwner) {
            username = it
        }
        viewModel.phoneNumber.observe(viewLifecycleOwner) {
            phoneNumber = it
        }
        viewModel.fetchProfile()

        val productId = firebaseRef.push().key!!
        var product: Product

        currentImageUri.let {
            storageRef.child(productId).putFile(it!!)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                           showToast(getString(R.string.uploading))
                            val imgUrl = url.toString()

                            product = Product(
                                username,
                                phoneNumber,
                                productName,
                                productPrice,
                                productDescription,
                                imgUrl
                            )

                            firebaseRef.child(productId).setValue(product)
                                .addOnCompleteListener {
                                    loadingDialog.hideLoading()
                                    val builder = AlertDialog.Builder(requireContext())
                                    builder.setTitle(getString(R.string.success))
                                        .setMessage(getString(R.string.product_uploaded))
                                        .setCancelable(true)
                                        .setPositiveButton("OK") { _, _ ->
                                            val fields = listOf(binding.edName, binding.edPrice, binding.edDescription)
                                            fields.forEach { field -> field.text!!.clear() }
                                            binding.ivPreview.setImageResource(R.drawable.baseline_image_search_24)
                                        }
                                    val alert = builder.create()
                                    alert.show()
                                }
                                .addOnFailureListener { error ->
                                    loadingDialog.hideLoading()
                                    Toast.makeText(
                                        context,
                                         "${error.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                        }
                }
        }
    }

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showToast(getString(R.string.no_image_selected))
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}