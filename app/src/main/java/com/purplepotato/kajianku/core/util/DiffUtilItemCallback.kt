package com.purplepotato.kajianku.core.util

import androidx.recyclerview.widget.DiffUtil
import com.purplepotato.kajianku.core.domain.Kajian

object DiffUtilItemCallback {
    val KAJIAN_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Kajian>() {
        override fun areItemsTheSame(oldItem: Kajian, newItem: Kajian): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Kajian, newItem: Kajian): Boolean {
            return oldItem.id == newItem.id
        }
    }
}