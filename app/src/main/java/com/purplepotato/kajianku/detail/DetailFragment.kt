package com.purplepotato.kajianku.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.util.Helpers
import com.purplepotato.kajianku.databinding.FragmentDetailBinding

class DetailFragment : Fragment(), View.OnClickListener {

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

        arguments?.let {
            val kajian = DetailFragmentArgs.fromBundle(it).kajian
            kajian?.let { item -> viewModel.setKajian(item) }
        }

        populateData()
        binding.btnFindLocation.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }

    private fun populateData() {
        with(binding) {
            val item = viewModel.getKajian()
            item?.let {
                imgPoster.load(item.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.image_placeholder)
                    error(R.drawable.ic_broken_image)
                }

                val recyclerAdapter = TagKajianRecyclerAdapter()
                binding.recyclerViewTag.apply {
                    adapter = recyclerAdapter
                    layoutManager = GridLayoutManager(requireContext(), 3)
                }
                recyclerAdapter.submitList(item.tagId)

                txtTitle.text = it.title
                txtSpeaker.text = it.speaker
                txtPlace.text = it.location
                txtDate.text = Helpers.convertTimeStampToDateFormat(it.startedAt)
                txtTime.text = getString(
                    R.string.time_format,
                    Helpers.convertTimeStampToTimeFormat(it.startedAt)
                )
                txtDescription.text = it.description
                txtRegisterUrl.text = it.registerUrl
            }

        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_find_location -> {
                val item = viewModel.getKajian()
                item?.let {
                    val gmmIntentUri = Uri.parse("geo:${it.latitude},${it.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    mapIntent.resolveActivity(requireContext().packageManager)?.let {
                        startActivity(mapIntent)
                    }
                }
            }

            R.id.btn_share -> {
                val mimeType = "text/plain"
                val item = viewModel.getKajian()
                ShareCompat.IntentBuilder.from(requireActivity()).apply {
                    setType(mimeType)
                    setChooserTitle(getString(R.string.share_kajian_title))
                    setText(getString(R.string.kajian_invitation, "\"${item?.title}\""))
                    startChooser()
                }
            }

            R.id.btn_back -> {
                activity?.onBackPressed()
            }
        }
    }
}