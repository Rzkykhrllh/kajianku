package com.purplepotato.kajianku.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val suggestedRecyclerAdapter = SuggestedKajianRecyclerAdapter()
        val popularRecyclerAdapter = PopularKajianRecyclerAdapter()

        binding.recyclerViewPopularKajian.apply {
            adapter = popularRecyclerAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.recyclerViewSuggestedKajian.apply {
            adapter = suggestedRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.listSuggestedKajian.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    suggestedRecyclerAdapter.submitList(result.data)
                }
                is Resource.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                }
            }
        })

        viewModel.listPopularKajian.observe(viewLifecycleOwner,{result->
            when(result){
                is Resource.Success -> {
                    popularRecyclerAdapter.submitList(result.data)
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                }
            }
        })
    }
}