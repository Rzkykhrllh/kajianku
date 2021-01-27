package com.purplepotato.kajianku.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.auth.AuthenticationActivity
import com.purplepotato.kajianku.core.session.Preferences
import com.purplepotato.kajianku.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext().applicationContext)
        )[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = Preferences(requireContext())
        // give icon to textView

        binding.tvChangePassword
            .setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_chevron_right_24,
                0
            );

        binding.tvLogOut
            .setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_chevron_right_24,
                0
            );

        binding.tvHistory
            .setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_chevron_right_24,
                0
            );

        binding.tvAboutUs
            .setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_chevron_right_24,
                0
            );

        binding.tvChangeEmail
            .setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_chevron_right_24,
                0
            );

        binding.tvLogOut.setOnClickListener {
            viewModel.logout()
        }


        // Navigation

        binding.tvChangePassword.setOnClickListener {
            Toast.makeText(context, "pindah ke change pass", Toast.LENGTH_LONG).show()

            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangePassword())
        }

        binding.tvAboutUs.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToTentangKamiFragment())
        }

        binding.tvHistory.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToRiwayatKajianFragment())
        }

        binding.tvChangeEmail.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChaneUsernameFragment())
        }


        // Log out
        viewModel.navigateToLogin.observe(viewLifecycleOwner, {
            pref.logout()
            val intent = Intent(activity, AuthenticationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finishAfterTransition()
        })

        // Set Username
//        Log.i("sharedprefence", "${sharedPref.getString("email", "ampasss cok")}")
        binding.tvEmail.text = pref.email
        binding.tvUsername.text = pref.name
    }

}