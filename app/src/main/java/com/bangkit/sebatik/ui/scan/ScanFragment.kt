package com.bangkit.sebatik.ui.scan

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.data.retrofit.ApiConfig
import com.bangkit.sebatik.databinding.FragmentScanBinding
import com.bangkit.sebatik.ui.product.ProductViewModel
import com.bangkit.sebatik.util.LoadingDialog
import com.bangkit.sebatik.util.ViewModelFactory
import com.bangkit.sebatik.util.getImageUri
import com.bangkit.sebatik.util.reduceFileImage
import com.bangkit.sebatik.util.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var loadingDialog: LoadingDialog
    private var currentImageUri: Uri? = null
    private lateinit var dataStore: DataStore<Preferences>

    private val viewModel: ScanViewModel by viewModels(){
        ViewModelFactory.getInstance(requireContext(), UserPreferences.getInstance(dataStore))
    }

    // TODO: Compress image before upload

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = requireContext().dataStore
        loadingDialog = LoadingDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().supportFragmentManager.popBackStack("home_fragment", 0)

        binding.apply {
            btnGallery.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            btnScan.setOnClickListener { uploadImage() }
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
            showToast("Tidak ada gambar yang dipilih")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            Log.d("Image URI", "showImage $it")
            binding.ivPreview.setImageURI(it)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let {
            val imageFile = uriToFile(it, requireContext()).reduceFileImage()
            getScanBatik(imageFile)
        }
    }

    private fun getScanBatik(file: File) {
        viewModel.scanBatik(file).observe(requireActivity()) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> loadingDialog.showLoading()
                    is Result.Success -> {
                        loadingDialog.hideLoading()
                        binding.tvResultName.text = it.data.batikName
                        binding.tvResultDescription.text = it.data.batikDesc
                        val action = ScanFragmentDirections.actionScanFragmentToDetailBatikFragment(
                            it.data.batikName.toString(),
                            currentImageUri.toString(),
                            it.data.batikDesc.toString()
                        )
                        val options = navOptions {
                            anim {
                                enter = R.anim.fade_in
                                exit = R.anim.fade_out
                                popEnter = R.anim.fade_in
                                popExit = R.anim.fade_out
                            }
                        }
                        findNavController().navigate(action, options)
                    }
                    is Result.Error -> {
                        loadingDialog.hideLoading()
                        showToast("Gagal")
                    }
                }
            }
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