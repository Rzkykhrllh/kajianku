package com.purplepotato.kajianku.saved_kajian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.session.Preferences
import com.purplepotato.kajianku.core.util.AlarmReceiver
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

        val alarmReceiver = AlarmReceiver()
        val pref = Preferences(requireContext())

        val recyclerAdapter = SavedKajianRecyclerAdapter()
        binding.recyclerViewSavedKajian.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        if (pref.shouldFetchSavedKajianFromRemote) {
            viewModel.queryAllSavedKajianFromRemote()
            pref.setShouldFetch(false)
        } else {
            viewModel.queryAllSavedKajianFromLocal()
        }

        binding.btnCariKajian.setOnClickListener {
            findNavController().navigate(SavedKajianFragmentDirections.actionSavedKajianFragmentToHomeFragment())
        }

        viewModel.listSavedKajian.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                    emptyState(false)
                }

                is Resource.Success -> {
                    if (result.data.isNullOrEmpty()) {
                        binding.txtSavedKajian.visibility = View.GONE
                        binding.recyclerViewSavedKajian.visibility = View.GONE
                        emptyState(true)
                    } else {
                        if (pref.shouldFetchSavedKajianFromRemote) {
                            result.data.forEach {
                                if (it.startedAt > System.currentTimeMillis()) {
                                    it.reminderId = pref.reminderId
                                    viewModel.insertListSavedKajianToLocal(it)
                                    alarmReceiver.setOneTimeAlarm(
                                        requireContext(),
                                        pref.reminderId,
                                        pref.reminderId,
                                        getString(
                                            R.string.message_kajian_reminder,
                                            it.title,
                                            "30 menit"
                                        ),
                                        it.startedAt + 1000 * 60 * 30
                                    )
                                } else {
                                    viewModel.deleteSavedKajianAndMoveToUserHistory(it)
                                }
                                pref.incrementReminderId()
                            }
                        } else {
                            result.data.forEach {
                                if (it.startedAt < System.currentTimeMillis()) {
                                    viewModel.deleteSavedKajianAndMoveToUserHistory(it)
                                }
                            }
                        }

                        recyclerAdapter.submitList(result.data)
                        binding.progressIndicator.visibility = View.GONE
                        emptyState(false)
                    }
                }

                is Resource.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    emptyState(true)
                }
            }
        })
    }

    private fun emptyState(state: Boolean) {
        if (state) {
            binding.tvBelum.visibility = View.VISIBLE
            binding.cariKajian.visibility = View.VISIBLE
            binding.btnCariKajian.visibility = View.VISIBLE
        } else {
            binding.tvBelum.visibility = View.GONE
            binding.cariKajian.visibility = View.GONE
            binding.btnCariKajian.visibility = View.GONE
        }
    }
}