package com.mason.kakao.masonsgallary.ui.detail

import com.mason.kakao.masonsgallary.model.data.ImageData

/**
 * Created by kakao on 2017. 11. 3..
 */

interface OnImageDeleteListener {
    fun onDelete(imageData: ImageData)
}