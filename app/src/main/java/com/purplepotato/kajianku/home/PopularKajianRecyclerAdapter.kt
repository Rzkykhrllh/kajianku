package com.purplepotato.kajianku.home

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
import com.purplepotato.kajianku.databinding.ItemPopularKajianBinding
import com.purplepotato.kajianku.detail.DetailFragment

class PopularKajianRecyclerAdapter :
    ListAdapter<Kajian, PopularKajianRecyclerAdapter.PopularKajianViewHolder>(
        DiffUtilItemCallback.KAJIAN_DIFF_CALLBACK
    ) {
    inner class PopularKajianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPopularKajianBinding.bind(itemView)
        fun bind(item: Kajian) {
            with(binding) {
                itemImgPoster.load(item.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.image_placeholder)
                    error(R.drawable.ic_broken_image)
                    transformations(RoundedCornersTransformation(16f))
                }
                itemTxtTitle.text = item.title
                itemTxtStatus.text = item.status
                root.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
                    action.kajian = item
                    root.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularKajianViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_popular_kajian, parent, false)
        return PopularKajianViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopularKajianViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}