package com.purplepotato.kajianku.saved_kajian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.databinding.FragmentSavedKajianBinding

class SavedKajianFragment : Fragment(),View.OnClickListener {

    private var _binding: FragmentSavedKajianBinding? = null

    private val binding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this,ViewModelFactory.getInstance())[SavedKajianViewModel::class.java]
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
        binding.btnSavedKajianToDetail.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_saved_kajian_to_detail ->{
                val action = SavedKajianFragmentDirections.actionSavedKajianFragmentToDetailFragment()
                findNavController().navigate(action)
            }
        }
    }

}