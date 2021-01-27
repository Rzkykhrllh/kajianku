package com.purplepotato.kajianku.profile.changeusername

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.auth.forgot.ForgotViewModel
import com.purplepotato.kajianku.databinding.FragmentChaneUsernameBinding
import com.purplepotato.kajianku.databinding.FragmentSignUpBinding


class ChaneUsernameFragment : Fragment() {

    private var _binding: FragmentChaneUsernameBinding? = null

    private val binding
        get() = _binding!!

    lateinit var viewModel : ChangeUsernameViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this).get(ChangeUsernameViewModel::class.java)

        _binding = FragmentChaneUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etGender.setOnKeyListener(null)

        setData()

        binding.btnUpdateData.setOnClickListener {
            updateData()
        }

        viewModel.navigateToProfile.observe(viewLifecycleOwner, Observer {
            if (it){
                Log.i("UPDATE-SHARED", "move to profile")

                findNavController().navigate(ChaneUsernameFragmentDirections
                    .actionChaneUsernameFragmentToProfileFragment())
            }
        })


    }

    private fun updateData() {

        var name = binding.etUsername.text.toString()


        val sharedPref = this.requireActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("nama", name).commit()
        viewModel.updateFirebase(name)

        val newName = sharedPref.getString("nama","ampassss")

        Log.i("UPDATE-SHARED", "nama : $name")
        Log.i("UPDATE-SHARED", "nama updated : $newName")


    }

    private fun setData() {
        val sharedPref = this.requireActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE)

        Log.i("UPDATE-SHARED", "set view data in change")

        binding.etGender.setText(sharedPref.getString("gender",""))
        binding.etEmail.setText(sharedPref.getString("email",""))
        binding.etBirthDate.setText(sharedPref.getString("birth",""))
        binding.etUsername.setText(sharedPref.getString("nama",""))

    }

}