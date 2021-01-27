package com.purplepotato.kajianku.auth.forgot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.auth.login.LoginViewModel
import com.purplepotato.kajianku.core.util.isValidEmail
import com.purplepotato.kajianku.databinding.FragmentForgotPasswordBinding

class ForgotPassword : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null

    private val binding
        get() = _binding!!

    lateinit var viewModel : ForgotViewModel
    lateinit var email : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(ForgotViewModel::class.java)

        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReset.setOnClickListener {
            if (validate()){
                viewModel.reset(email)
            }
        }

        binding.tvBackToLogin.setOnClickListener {
            findNavController().navigate(ForgotPasswordDirections.actionForgotPasswordToLoginFragment())
        }

        viewModel.navigateToLoginFragment.observe(viewLifecycleOwner, Observer {
            if (it=="true"){

                findNavController().navigate(ForgotPasswordDirections.actionForgotPasswordToLoginFragment())
            } else{
                Toast.makeText(context, "Gagal mengirim permintaan ke email", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {  state ->
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
        email = binding.etEmail.text.toString()

        if (email.isNullOrEmpty()) {
            binding.etEmail.error = "Mohon masukkan email anda"
            return false
        }

        if (!email.isValidEmail()) {
            binding.etEmail.error = "Email yang anda masukkan tidak valid"
            return false
        }

        return true

    }


}