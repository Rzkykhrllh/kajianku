package com.purplepotato.kajianku.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.core.domain.Kajian
import com.purplepotato.kajianku.core.helper.DiffUtilItemCallback
import com.purplepotato.kajianku.databinding.ItemPopularKajianBinding

class PopularKajianRecyclerAdapter :
    ListAdapter<Kajian, PopularKajianRecyclerAdapter.PopularKajianViewHolder>(
        DiffUtilItemCallback.KAJIAN_DIFF_CALLBACK
    ) {
    inner class PopularKajianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPopularKajianBinding.bind(itemView)
        fun bind(item: Kajian) {
            with(binding) {
                // tempat assign ke item recycler nya
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