package com.purplepotato.kajianku.home.allkajian

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.databinding.FragmentAllKajianBinding

class AllKajianFragment : Fragment(),View.OnClickListener {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackToHome.setOnClickListener(this)

        val recyclerAdapter = AllKajianRecyclerAdapter()

        binding.recyclerViewAllKajian.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.listAllKajian.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Success -> {
                    showLoading(false)

                    recyclerAdapter.submitList(result.data)
                }
                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(activity, result.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressIndicator.visibility = View.VISIBLE
        } else {
            binding.progressIndicator.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_back_to_home -> {
                requireActivity().onBackPressed()
            }
        }
    }
}