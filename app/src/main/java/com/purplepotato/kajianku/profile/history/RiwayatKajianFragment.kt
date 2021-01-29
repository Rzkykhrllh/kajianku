package com.purplepotato.kajianku.profile.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.databinding.FragmentRiwayatKajianBinding

class RiwayatKajianFragment : Fragment() {

    private var _binding: FragmentRiwayatKajianBinding? = null

    private val binding: FragmentRiwayatKajianBinding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext().applicationContext)
        )[RiwayatKajianViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRiwayatKajianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = KajianHistoryRecyclerAdapter()

        binding.recyclerViewKajianHistory.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.listKajianHistory.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Success -> {
                    showLoading(false)
                    if (result.data.isNullOrEmpty()) {
                        binding.emptyIndicator.visibility = View.VISIBLE
                    } else {
                        recyclerAdapter.submitList(result.data)
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Error -> {
                    Toast.makeText(activity, result.message, Toast.LENGTH_LONG).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}