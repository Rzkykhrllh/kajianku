package com.purplepotato.kajianku.auth.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.purplepotato.kajianku.MainActivity
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.util.isValidEmail
import com.purplepotato.kajianku.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null

    private val binding
        get() = _binding!!

    lateinit var name: String
    lateinit var password: String
    lateinit var email: String
    lateinit var gender: String
    lateinit var birth: String

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance())[SignUpViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            if (validate()) {
                viewModel.signup(name, birth, gender, email, password)
            }
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Move to homme", Toast.LENGTH_LONG).show()
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                activity?.finishAfterTransition()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validate(): Boolean {

        // Log.i("validation", "im here")

        name = binding.etName.text.toString()
        email = binding.etEmail.text.toString()
        birth = binding.etBirthDate.text.toString()
        password = binding.etPassword.text.toString()
        gender = binding.etGender.text.toString()
        // Log.i("validation", "$name")

        // Empty Check
        if (name.isNullOrEmpty()) {
            binding.etName.error = "Mohon masukkan nama anda"
            return false
        }

        if (birth.isNullOrEmpty()) {
            binding.etBirthDate.error = "Mohon masukkan tanggal lahir anda"
            return false
        }

        if (gender.isNullOrEmpty()) {
            binding.etGender.error = "Mohon masukkan jenis kelamin anda"
            return false
        }

        if (email.isNullOrEmpty()) {
            binding.etEmail.error = "Mohon masukkan email anda"
            return false
        }

        if (password.isNullOrEmpty()) {
            binding.etPassword.error = "Mohon massukan passord anda"
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