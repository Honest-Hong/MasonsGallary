package com.mason.kakao.masonsgallary.util

import com.mason.kakao.masonsgallary.R
import com.mason.kakao.masonsgallary.model.data.Tag

/**
 * Created by kakao on 2017. 10. 31..
 */

class TagUtil {
    companion object {
        fun getTagById(id: Int): Tag {
            when (id) {
                R.id.menu_ryan -> return Tag.Ryan
                R.id.menu_apeach -> return Tag.Apeach
                R.id.menu_muzi -> return Tag.Muzi
                R.id.menu_frodo -> return Tag.Frodo
                R.id.menu_neo -> return Tag.Neo
                R.id.menu_tube -> return Tag.Tube
                R.id.menu_jay_g -> return Tag.Jay_G
                R.id.menu_con -> return Tag.Con
            }
            return Tag.All
        }
    }
}
