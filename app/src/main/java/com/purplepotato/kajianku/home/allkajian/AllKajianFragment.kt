package com.purplepotato.kajianku.home.allkajian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.databinding.FragmentAllKajianBinding

class AllKajianFragment : Fragment() {

    private var _binding: FragmentAllKajianBinding? = null

    private val binding: FragmentAllKajianBinding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext().applicationContext)
        )[AllKajianViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllKajianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}