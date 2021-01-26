package com.purplepotato.kajianku.detail

import androidx.lifecycle.ViewModel
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian

class DetailViewModel(private val repository: KajianRepository) : ViewModel() {

    private var _kajian: Kajian? = null

    fun setKajian(kajian: Kajian) {
        this._kajian = kajian
    }

    fun getKajian() = _kajian

}