package com.purplepotato.kajianku.saved_kajian

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.core.domain.Kajian
import com.purplepotato.kajianku.core.util.DiffUtilItemCallback
import com.purplepotato.kajianku.databinding.ItemSuggestedKajianBinding

class SavedKajianRecyclerAdapter :
    ListAdapter<Kajian, SavedKajianRecyclerAdapter.SavedKajianViewHolder>(
        DiffUtilItemCallback.KAJIAN_DIFF_CALLBACK
    ) {

    inner class SavedKajianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSuggestedKajianBinding.bind(itemView)
        fun bind(item: Kajian) {
            with(binding) {
                //assign value to item recycler view
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedKajianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suggested_kajian, parent, false)
        return SavedKajianViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SavedKajianRecyclerAdapter.SavedKajianViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}