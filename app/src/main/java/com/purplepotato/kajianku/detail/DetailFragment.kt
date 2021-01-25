package com.purplepotato.kajianku.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance())[DetailViewModel::class.java]
    }

    private var _binding: FragmentDetailBinding? = null

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = TagKajianRecyclerAdapter()
        binding.recyclerViewTag.apply {
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        arguments?.let {
            val kajian = DetailFragmentArgs.fromBundle(it).kajian
            populateData()
        }

    }

    private fun populateData() {
        // detail
    }
}