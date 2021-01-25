package com.purplepotato.kajianku.profile.changepassword

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.auth.forgot.ForgotViewModel
import com.purplepotato.kajianku.databinding.ChangePasswordFragmentBinding
import com.purplepotato.kajianku.databinding.FragmentForgotPasswordBinding
import com.purplepotato.kajianku.databinding.FragmentProfileBinding
import com.purplepotato.kajianku.profile.ProfileViewModel

class ChangePassword : Fragment() {

    private var _binding: ChangePasswordFragmentBinding? = null

    private val binding
        get() = _binding!!

    lateinit var viewModel : ChangePasswordViewModel

    lateinit var oldPassword : String
    lateinit var newPassword : String
    lateinit var retypePassword : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)

        _binding = ChangePasswordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChangePass.setOnClickListener {
            if (validate()){
                viewModel.changepass(oldPassword, newPassword)
            }
        }

        viewModel.navigateToProfile.observe(viewLifecycleOwner, Observer {
            if (viewModel.navigateToProfile.value == true){
                findNavController().navigate(ChangePasswordDirections.actionChangePasswordToProfileFragment())
            }
        })
    }

    private fun validate(): Boolean {

        oldPassword = binding.etOldPassword.text.toString()
        newPassword = binding.etNewPassword.text.toString()
        retypePassword = binding.etRetypePassword.text.toString()

        if (oldPassword.isNullOrEmpty()){
            binding.etOldPassword.setError("Mohon masukkan password lama anda")
            return false
        }

        if (newPassword.isNullOrEmpty()){
            binding.etNewPassword.setError("Mohon masukkan password baru anda")
            return false
        }

        if (retypePassword.isNullOrEmpty()){
            binding.etRetypePassword.setError("Mohon masukkan kembali password baru anda")
            return false
        }

        if (newPassword != retypePassword){
            binding.etRetypePassword.setError("Mohon cek kemabli password yang anda masukkan")
            return false
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}