package com.mason.kakao.masonsgallary.ui.detail

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.mason.kakao.masonsgallary.model.data.ImageData
import com.mason.kakao.masonsgallary.util.TagUtil

/**
 * Created by kakao on 2017. 11. 3..
 */

class ImageDetailViewModel(
        val imageData: ImageData
) {
    val tagImage = ObservableInt(TagUtil.getResourceIdByTag(imageData.tag))
    val image = ObservableField<String>(imageData.path)
    val isDeleted = ObservableBoolean(false)

    fun onDelete() {
        isDeleted.set(true)
    }
}