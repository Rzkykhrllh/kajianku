package com.purplepotato.kajianku.profile.changeusername

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.databinding.FragmentChaneUsernameBinding
import com.purplepotato.kajianku.databinding.FragmentSignUpBinding


class ChaneUsernameFragment : Fragment() {

    private var _binding: FragmentChaneUsernameBinding? = null

    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChaneUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etGender.setOnKeyListener(null)

        setData()
    }

    private fun setData() {
        val sharedPref = this.requireActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE)

        Log.i("sharedprefence", "${sharedPref.getString("email","ampasss cok")}")

        binding.etGender.setText(sharedPref.getString("gender",""))
        binding.etEmail.setText(sharedPref.getString("email",""))
        binding.etBirthDate.setText(sharedPref.getString("birth",""))
        binding.etUsername.setText(sharedPref.getString("nama",""))

    }

}