package com.purplepotato.kajianku.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.purplepotato.kajianku.R
import com.purplepotato.kajianku.core.domain.Tag
import com.purplepotato.kajianku.core.util.DiffUtilItemCallback
import com.purplepotato.kajianku.databinding.ItemTagKajianBinding

class TagKajianRecyclerAdapter :
    ListAdapter<Tag, TagKajianRecyclerAdapter.TagKajianViewHolder>(DiffUtilItemCallback.TAG_KAJIAN_DIFF_CALLBACK) {

    inner class TagKajianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemTagKajianBinding.bind(itemView)
        fun bind(item : Tag){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagKajianViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag_kajian,parent,false)
        return TagKajianViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagKajianViewHolder, position: Int) {

    }
}