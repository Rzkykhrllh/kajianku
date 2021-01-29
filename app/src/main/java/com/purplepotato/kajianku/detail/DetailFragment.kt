package com.purplepotato.kajianku.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.ViewModelFactory
import com.purplepotato.kajianku.core.session.Preferences
import com.purplepotato.kajianku.core.util.AlarmReceiver
import com.purplepotato.kajianku.core.util.Helpers
import com.purplepotato.kajianku.core.util.showLongToastMessage
import com.purplepotato.kajianku.databinding.FragmentDetailBinding

class DetailFragment : Fragment(), View.OnClickListener {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireContext().applicationContext)
        )[DetailViewModel::class.java]
    }

    private var _binding: FragmentDetailBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var alarmReceiver: AlarmReceiver

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

            populateData()
        }

        viewModel.isKajianAlreadySaved.observe(viewLifecycleOwner, {
            if (it == true) {
                showCancelKajianButton(true)
            } else {
                showCancelKajianButton(false)
            }
        })

        binding.btnFindLocation.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnSaveKajian.setOnClickListener(this)
        binding.btnCancelKajian.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
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

                txtStatus.text = it.status
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
                txtTime.text = it.time
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

            R.id.btn_save_kajian -> {

                var saved = 0
                val listItems = arrayOf("30 menit sebelum", "1 jam sebelum", "1 hari sebelum")
                val mBuilder = AlertDialog.Builder(requireContext())

                mBuilder.setTitle("Set Reminder : ")
                mBuilder.setSingleChoiceItems(listItems, 0) { _, i ->
                    saved = i
                }

                mBuilder.setNeutralButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

                mBuilder.setPositiveButton("Simpan") { dialog, which ->
                    Toast.makeText(
                        requireContext(),
                        "item terpilih adalah ${listItems[saved]}",
                        Toast.LENGTH_LONG
                    ).show()
                    var timeReducer: Long = 0
                    var stringTime = ""
                    when (saved) {
                        THIRTY_MINUTES_BEFORE -> {
                            timeReducer = 1000 * 60 * 30
                            stringTime = "30 menit"
                        }

                        ONE_HOUR_BEFORE -> {
                            timeReducer = 1000 * 60 * 60
                            stringTime = "30 jam"
                        }

                        ONE_DAY_BEFORE -> {
                            timeReducer = 1000 * 60 * 60 * 24
                            stringTime = "1 hari"
                        }
                    }

                    val pref = Preferences(requireContext())
                    val item = viewModel.getKajian()
                    item?.let {
                        it.reminderId = pref.reminderId
                        viewModel.setKajian(it)
                        alarmReceiver.setOneTimeAlarm(
                            requireContext(),
                            pref.reminderId,
                            pref.reminderId,
                            getString(R.string.message_kajian_reminder, it.title,stringTime),
                            it.startedAt - timeReducer
                        )
                    }

                    pref.incrementReminderId()
                    viewModel.insertSavedKajian()
                    requireContext().showLongToastMessage(getString(R.string.add_reminder_succeed_message))
                }

                val mDialog = mBuilder.create()
                mDialog.show()
            }

            R.id.btn_cancel_kajian -> {
                val item = viewModel.getKajian()
                alarmReceiver.cancelAlarm(
                    requireContext(),
                    item?.reminderId!!
                )
                viewModel.deleteSavedKajian()
                showCancelKajianButton(false)
                requireContext().showLongToastMessage(getString(R.string.delete_reminder_message))
            }
        }
    }

    private fun showCancelKajianButton(state: Boolean) {
        if (state) {
            binding.btnSaveKajian.visibility = View.GONE
            binding.btnCancelKajian.visibility = View.VISIBLE
        } else {
            binding.btnSaveKajian.visibility = View.VISIBLE
            binding.btnCancelKajian.visibility = View.GONE
        }
    }

    companion object {
        private const val THIRTY_MINUTES_BEFORE = 0
        private const val ONE_HOUR_BEFORE = 1
        private const val ONE_DAY_BEFORE = 2
    }
}