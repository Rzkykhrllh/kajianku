package com.purplepotato.kajianku.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.core.domain.Kajian
import com.purplepotato.kajianku.core.util.DiffUtilItemCallback
import com.purplepotato.kajianku.core.util.Helpers
import com.purplepotato.kajianku.databinding.ItemKajianBinding

class SuggestedKajianRecyclerAdapter :
    ListAdapter<Kajian, SuggestedKajianRecyclerAdapter.SuggestedKajianViewHolder>(
        DiffUtilItemCallback.KAJIAN_DIFF_CALLBACK
    ) {

    inner class SuggestedKajianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemKajianBinding.bind(itemView)
        fun bind(item: Kajian) {
            with(binding) {
                img.itemImgPoster.load(item.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.image_placeholder)
                    error(R.drawable.ic_broken_image)
                }

                itemTxtTitle.text = item.title
                itemTxtSpeaker.text = item.speaker
                itemTxtPlace.text = item.location
                itemTxtTime.text = root.context.getString(
                    R.string.time_format,
                    Helpers.convertTimeStampToTimeFormat(item.startedAt)
                )

                root.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                    action.kajian = item
                    root.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedKajianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kajian, parent, false)
        return SuggestedKajianViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuggestedKajianViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}