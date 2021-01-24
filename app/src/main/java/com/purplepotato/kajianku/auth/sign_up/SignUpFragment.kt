package com.purplepotato.kajianku.auth.sign_up

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.purplepotato.kajianku.MainActivity
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.auth.login.LoginFragmentDirections
import com.purplepotato.kajianku.core.util.isValidEmail
import com.purplepotato.kajianku.databinding.FragmentSignUpBinding
import java.text.SimpleDateFormat
import java.util.*


class SignUpFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignUpBinding? = null

    private val binding
        get() = _binding!!

    lateinit var name: String
    lateinit var password: String
    lateinit var email: String
    lateinit var gender: String
    lateinit var birth: String


    var cal = Calendar.getInstance()

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

        binding.btnToLogin.setOnClickListener(this)

        binding.btnSignUp.setOnClickListener {
            if (validate()) {
                viewModel.signup(name, birth, gender, email, password)
            }
        }

        binding.btnToLogin.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }

        // Pindah ke halaman Home
        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Move to home", Toast.LENGTH_LONG).show()
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                activity?.finishAfterTransition()
            }
        })



        // Buat objek date
        val date =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel()
            }

        binding.etBirthDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }


        // Fungsi buat spinner
        val genderList = resources.getStringArray(R.array.gender_list)
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, genderList)
        binding.spinnerGender.adapter = spinnerAdapter

        binding.spinnerGender.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                Toast.makeText(context, genderList[position], Toast.LENGTH_SHORT).show()
                gender = genderList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
            }

        }
    }


    // Fungsi untuk mengubah tampilan pada view
    private fun updateLabel() {
        val myFormat = "MM/dd/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.etBirthDate.setText(sdf.format(cal.getTime()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun validate(): Boolean {

        name = binding.etName.text.toString()
        email = binding.etEmail.text.toString()
        birth = binding.etBirthDate.text.toString()
        password = binding.etPassword.text.toString()
//        gender = binding.etGender.text.toString()

        // Empty Check
        if (name.isNullOrEmpty()) {
            binding.etName.error = "Mohon masukkan nama anda"
            return false
        }

        if (birth.isNullOrEmpty()) {
            binding.etBirthDate.error = "Mohon masukkan tanggal lahir anda"
            return false
        }

        /*if (gender.isNullOrEmpty()) {
            binding.etGender.error = "Mohon masukkan jenis kelamin anda"
            return false
        }*/

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

        Toast.makeText(context, "nama : $name \ndate : $birth \ngender : $gender", Toast.LENGTH_LONG).show()
        return true
    }

    override fun onClick(v: View?) {
//        TODO("Not yet implemented")
    }
}