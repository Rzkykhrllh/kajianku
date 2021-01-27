package com.purplepotato.kajianku.profile.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.purplepotato.kajianku.databinding.ChangePasswordFragmentBinding

class ChangePassword : Fragment() {

    private var _binding: ChangePasswordFragmentBinding? = null

    private val binding
        get() = _binding!!

    lateinit var viewModel: ChangePasswordViewModel

    lateinit var oldPassword: String
    lateinit var newPassword: String
    lateinit var retypePassword: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)

        _binding = ChangePasswordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChangePass.setOnClickListener {
            if (validate()) {
                viewModel.changepass(oldPassword, newPassword)
            }
        }

        viewModel.navigateToProfile.observe(viewLifecycleOwner, Observer {
            if (viewModel.navigateToProfile.value == true) {
                findNavController().navigate(ChangePasswordDirections.actionChangePasswordToProfileFragment())
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { state ->
            if (state) {
                binding.loadingFrame.visibility = View.VISIBLE
                binding.loginProgressBar.visibility = View.VISIBLE
            } else {
                binding.loadingFrame.visibility = View.GONE
                binding.loginProgressBar.visibility = View.GONE
            }
        })
    }

    private fun validate(): Boolean {

        oldPassword = binding.etOldPassword.text.toString()
        newPassword = binding.etNewPassword.text.toString()
        retypePassword = binding.etRetypePassword.text.toString()

        if (oldPassword.isNullOrEmpty()) {
            binding.etOldPassword.setError("Mohon masukkan password lama anda")
            return false
        }

        if (newPassword.isNullOrEmpty()) {
            binding.etNewPassword.setError("Mohon masukkan password baru anda")
            return false
        }

        if (retypePassword.isNullOrEmpty()) {
            binding.etRetypePassword.setError("Mohon masukkan kembali password baru anda")
            return false
        }

        if (newPassword != retypePassword) {
            binding.etRetypePassword.setError("Mohon cek kembali password yang anda masukkan")
            return false
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}