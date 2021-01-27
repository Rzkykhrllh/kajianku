package com.purplepotato.kajianku.saved_kajian

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.core.domain.Kajian
import com.purplepotato.kajianku.core.util.DiffUtilItemCallback
import com.purplepotato.kajianku.core.util.Helpers
import com.purplepotato.kajianku.databinding.ItemKajianBinding

class SavedKajianRecyclerAdapter :
    ListAdapter<Kajian, SavedKajianRecyclerAdapter.SavedKajianViewHolder>(
        DiffUtilItemCallback.KAJIAN_DIFF_CALLBACK
    ) {

    inner class SavedKajianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemKajianBinding.bind(itemView)
        fun bind(item: Kajian) {
            with(binding) {
                itemImgPoster.load(item.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.image_placeholder)
                    error(R.drawable.ic_broken_image)
                    transformations(RoundedCornersTransformation(8f))
                }

                itemTxtTitle.text = item.title
                itemTxtSpeaker.text = item.speaker
                itemTxtPlace.text = item.location
                itemTxtDateTime.text = root.context.getString(
                    R.string.date_time_format,
                    Helpers.convertTimeStampToDateTimeFormat(item.startedAt)
                )

                root.setOnClickListener {
                    val action =
                        SavedKajianFragmentDirections.actionSavedKajianFragmentToDetailFragment()
                    action.kajian = item
                    root.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedKajianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kajian, parent, false)
        return SavedKajianViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SavedKajianRecyclerAdapter.SavedKajianViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}