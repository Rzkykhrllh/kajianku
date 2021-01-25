package com.purplepotato.kajianku.core.util

import androidx.recyclerview.widget.DiffUtil
import com.purplepotato.kajianku.core.domain.Kajian
import com.purplepotato.kajianku.core.domain.Tag

object DiffUtilItemCallback {
    val KAJIAN_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Kajian>() {
        override fun areItemsTheSame(oldItem: Kajian, newItem: Kajian): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Kajian, newItem: Kajian): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val TAG_KAJIAN_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Tag>() {
        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem.tag == newItem.tag
        }

    }
}