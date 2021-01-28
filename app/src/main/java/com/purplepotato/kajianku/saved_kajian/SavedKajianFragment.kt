package com.purplepotato.kajianku.saved_kajian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.databinding.FragmentSavedKajianBinding

class SavedKajianFragment : Fragment() {

    private var _binding: FragmentSavedKajianBinding? = null

    private val binding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext().applicationContext)
        )[SavedKajianViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedKajianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = SavedKajianRecyclerAdapter()
        binding.recyclerViewSavedKajian.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btnCariKajian.setOnClickListener {
         findNavController().navigate(SavedKajianFragmentDirections.actionSavedKajianFragmentToHomeFragment())
        }

        viewModel.listSavedKajian.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    recyclerAdapter.submitList(result.data)

                    if (result.data.isNullOrEmpty()){
                        binding.txtSavedKajian.visibility = View.GONE
                        binding.recyclerViewSavedKajian.visibility = View.GONE
                        binding.viewNotSaved.visibility = View.VISIBLE
                    } else{
                        binding.viewNotSaved.visibility = View.GONE
                    }
                }

                is Resource.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                }
            }
        })
    }
}