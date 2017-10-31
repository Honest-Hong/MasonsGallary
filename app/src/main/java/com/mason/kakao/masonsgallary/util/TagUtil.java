package com.mason.kakao.masonsgallary.util;

import android.view.MenuItem;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.model.data.Tag;

/**
 * Created by kakao on 2017. 10. 31..
 */

public final class TagUtil {
    public static Tag getTagByMenu(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.menu_ryan:
                return Tag.Ryan;
            case R.id.menu_apeach:
                return Tag.Apeach;
            case R.id.menu_muzi:
                return Tag.Muzi;
            case R.id.menu_frodo:
                return Tag.Frodo;
            case R.id.menu_neo:
                return Tag.Neo;
            case R.id.menu_tube:
                return Tag.Tube;
            case R.id.menu_jay_g:
                return Tag.Jay_G;
            case R.id.menu_con:
                return Tag.Con;
        }
        return Tag.All;
    }

    public static int getDrawableResourceByTag(Tag tag) {
        int drawableId = 0;
        switch(tag) {
            case Ryan:
                drawableId = R.drawable.tag_ryan;
                break;
            case Muzi:
                drawableId = R.drawable.tag_muzi;
                break;
            case Apeach:
                drawableId = R.drawable.tag_apeach;
                break;
            case Frodo:
                drawableId = R.drawable.tag_frodo;
                break;
            case Neo:
                drawableId = R.drawable.tag_neo;
                break;
            case Tube:
                drawableId = R.drawable.tag_tube;
                break;
            case Jay_G:
                drawableId = R.drawable.tag_jay_g;
                break;
            case Con:
                drawableId = R.drawable.tag_con;
                break;
        }
        return drawableId;
    }
}
