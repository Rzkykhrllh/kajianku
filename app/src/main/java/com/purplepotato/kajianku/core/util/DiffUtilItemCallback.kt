package com.purplepotato.kajianku.core.util

import androidx.recyclerview.widget.DiffUtil
import com.purplepotato.kajianku.core.domain.Kajian

object DiffUtilItemCallback {
    val KAJIAN_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Kajian>() {
        override fun areItemsTheSame(oldItem: Kajian, newItem: Kajian): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Kajian, newItem: Kajian): Boolean {
            return oldItem.title == newItem.title && oldItem.description == oldItem.description
        }
    }

    val TAG_KAJIAN_DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}