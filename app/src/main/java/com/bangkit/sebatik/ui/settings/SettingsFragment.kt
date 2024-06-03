package com.bangkit.sebatik.ui.settings

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.databinding.FragmentSettingsBinding
import com.bangkit.sebatik.ui.login.LoginActivity
import com.bangkit.sebatik.ui.team.TeamFragment
import com.bangkit.sebatik.util.LoadingDialog
import com.bangkit.sebatik.util.ViewModelFactory

class SettingsFragment() : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var dataStore: DataStore<Preferences>
    private val viewModel by viewModels<SettingsViewModel>() {
        ViewModelFactory.getInstance(requireContext(), UserPreferences.getInstance(dataStore))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = requireContext().dataStore
        loadingDialog = LoadingDialog(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchProfile()
        binding.apply {
            tvLogout.setOnClickListener { logout() }
            tvOurTeam.setOnClickListener { navigateToTeam() }
        }
    }

    private fun navigateToTeam() {
        val options = navOptions {
            anim {
                enter = R.anim.fade_in
                popExit = R.anim.fade_out
            }
        }
        findNavController().navigate(R.id.action_settingsFragment_to_teamFragment, null, options)
    }

    private fun logout() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_message))
            .setCancelable(true)
            .setNegativeButton(getString(R.string.no)) {
                    dialog, _ -> dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
//                loadingDialog.showLoading()
                viewModel.logout()
//                loadingDialog.hideLoading()
                navigateToActivity()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun fetchProfile() {
        viewModel.username.observe(viewLifecycleOwner) { username ->
            binding.tvUsername.text = username
        }

        viewModel.email.observe(viewLifecycleOwner) { email ->
            binding.tvEmail.text = email
        }

        viewModel.phoneNumber.observe(viewLifecycleOwner) { phoneNumber ->
            binding.tvPhoneNumber.text = getString(R.string.country_code)+"$phoneNumber"
        }

        viewModel.fetchProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private fun navigateToActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}