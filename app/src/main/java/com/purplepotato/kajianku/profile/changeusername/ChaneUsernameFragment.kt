package com.purplepotato.kajianku.profile.changeusername

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.purplepotato.kajianku.core.session.Preferences
import com.purplepotato.kajianku.databinding.FragmentChaneUsernameBinding

class ChaneUsernameFragment : Fragment() {

    private var _binding: FragmentChaneUsernameBinding? = null

    private val binding
        get() = _binding!!

    lateinit var viewModel: ChangeUsernameViewModel
    private lateinit var preferences: Preferences

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
        Preferences(requireContext())
        setData()

        binding.btnUpdateData.setOnClickListener {
            updateData()
        }

        viewModel.navigateToProfile.observe(viewLifecycleOwner, Observer {
            if (it) {
//                Log.i("UPDATE-SHARED", "move to profile")
                findNavController().navigate(
                    ChaneUsernameFragmentDirections
                        .actionChaneUsernameFragmentToProfileFragment()
                )
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

    private fun updateData() {
        val name = binding.etUsername.text.toString()

        preferences.setName(name)
        viewModel.updateFirebase(name)

//        val newName = sharedPref.getString("nama","ampassss")
//        Log.i("UPDATE-SHARED", "nama : $name")
//        Log.i("UPDATE-SHARED", "nama updated : $newName")
    }

    private fun setData() {
//        Log.i("UPDATE-SHARED", "set view data in change")

        binding.etGender.setText(preferences.gender)
        binding.etEmail.setText(preferences.email)
        binding.etBirthDate.setText(preferences.birth)
        binding.etUsername.setText(preferences.name)
    }

}