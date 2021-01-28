package com.purplepotato.kajianku.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.purplepotato.kajianku.MainActivity
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.session.Preferences
import com.purplepotato.kajianku.core.util.isValidEmail
import com.purplepotato.kajianku.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var password: String
    private lateinit var email: String

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext().applicationContext)
        )[LoginViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        viewModel.navigateToHome.observe(viewLifecycleOwner, {
            if (it) {
                moveToHome()
//                saveUserDataToPreferences()
            }
        })
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
        binding.tvForgetPassword.setOnClickListener(this)

        viewModel.isLoading.observe(viewLifecycleOwner, { state ->
            showLoading(state)
        })
    }

    private fun moveToHome() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finishAfterTransition()
    }

    private fun saveUserDataToPreferences() {
        val pref = Preferences(requireContext())
        pref.setGender(viewModel.gender as String)
        pref.setEmail(viewModel.email1 as String)
        pref.setBirth(viewModel.birth as String)
        pref.setName(viewModel.name as String)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> {
                val test = validate()
                Log.i("logincess", "hasil validasi ${validate()}")

                if (test) {
                    viewModel.login(email, password)
                }
            }

            R.id.btn_to_sign_up -> {
                val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                findNavController().navigate(action)
            }

            R.id.tv_forget_password -> {
                val action = LoginFragmentDirections.actionLoginFragmentToForgotPassword()
                findNavController().navigate(action)
            }
        }
    }

    private fun validate(): Boolean {

        email = binding.etEmail.text.toString()
        password = binding.etPassword.text.toString()

        // Empty Check
        if (email.isEmpty()) {
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
            binding.etPassword.error = "Password yang anda masukkan kurang dari 8 karakter"
            return false
        }
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.loadingFrame.visibility = View.VISIBLE
            binding.loginProgressBar.visibility = View.VISIBLE
        } else {
            binding.loadingFrame.visibility = View.GONE
            binding.loginProgressBar.visibility = View.GONE
        }
    }
}