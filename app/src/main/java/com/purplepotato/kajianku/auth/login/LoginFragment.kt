package com.purplepotato.kajianku.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.purplepotato.kajianku.MainActivity
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.util.isValidEmail
import com.purplepotato.kajianku.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null

    private val binding
        get() = _binding!!


    private lateinit var password: String
    private lateinit var email: String

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance())[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener(this)
        binding.btnToSignUp.setOnClickListener(this)

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Move to home", Toast.LENGTH_LONG).show()

                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                activity?.finishAfterTransition()
            }
        })

        binding.tvForgetPassord.setOnClickListener {
            Toast.makeText(context, "gelud yokkk", Toast.LENGTH_SHORT).show()
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPassword())
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> {
                if (validate()) {
                    viewModel.login(email, password)
                }

            }

            R.id.tv_forget_passord -> {
                Toast.makeText(context, "gelud yokkk", Toast.LENGTH_SHORT).show()
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPassword())
            }

            R.id.btn_to_sign_up -> {
                val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun validate(): Boolean {

        email = binding.etEmail.text.toString()
        password = binding.etPassword.text.toString()

        // Empty Check

        if (email.isNullOrEmpty()) {
            binding.etEmail.error = "Mohon masukkan email anda"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Mohon masukkan passord anda"
            return false
        }

        // Format check

        if (!email.isValidEmail()) {
            binding.etEmail.error = "Email yang anda masukkan tidak valid"
            return false
        }

        if (password.length < 8) {
            binding.etEmail.error = "Password yang anda masukkan kurang dari 8 karakter"
            return false
        }
        return true
    }
}