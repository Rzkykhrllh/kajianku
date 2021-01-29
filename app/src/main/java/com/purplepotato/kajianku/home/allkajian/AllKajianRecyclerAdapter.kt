package com.purplepotato.kajianku.home.allkajian

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
import com.purplepotato.kajianku.home.HomeFragmentDirections

class AllKajianRecyclerAdapter :
    ListAdapter<Kajian, AllKajianRecyclerAdapter.AllKajianViewHolder>(
        DiffUtilItemCallback.KAJIAN_DIFF_CALLBACK
    ) {

    inner class AllKajianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
                itemTxtDateTime.text = "${Helpers.convertTimeStampToDateTimeFormat(item.startedAt)} | ${item.time}"

                root.setOnClickListener {
                    val action = AllKajianFragmentDirections.actionAllKajianToDetailFragment()
                    action.kajian = item
                    root.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllKajianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kajian, parent, false)
        return AllKajianViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllKajianViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}