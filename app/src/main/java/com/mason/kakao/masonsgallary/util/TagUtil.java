package com.mason.kakao.masonsgallary.util;

import android.view.MenuItem;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.model.data.Tag;

/**
 * Created by kakao on 2017. 10. 31..
 */

public final class TagUtil {
    public static Tag getTagByMenu(MenuItem menuItem) {
        Tag tag = Tag.All;
        switch (menuItem.getItemId()) {
            case R.id.menu_ryan:
                tag = Tag.Ryan;
                break;
            case R.id.menu_muzi:
                tag = Tag.Muzi;
                break;
            case R.id.menu_apeach:
                tag = Tag.Apeach;
                break;
            case R.id.menu_frodo:
                tag = Tag.Frodo;
                break;
            case R.id.menu_neo:
                tag = Tag.Neo;
                break;
            case R.id.menu_tube:
                tag = Tag.Tube;
                break;
            case R.id.menu_jay_g:
                tag = Tag.Jay_G;
                break;
            case R.id.menu_con:
                tag = Tag.Con;
                break;
        }
        return tag;
    }

    public static int getResourceIdByTag(Tag tag) {
        int resourceId = 0;
        switch(tag) {
            case Ryan:
                resourceId = R.drawable.tag_ryan;
                break;
            case Muzi:
                resourceId = R.drawable.tag_muzi;
                break;
            case Apeach:
                resourceId = R.drawable.tag_apeach;
                break;
            case Frodo:
                resourceId = R.drawable.tag_frodo;
                break;
            case Neo:
                resourceId = R.drawable.tag_neo;
                break;
            case Tube:
                resourceId = R.drawable.tag_tube;
                break;
            case Jay_G:
                resourceId = R.drawable.tag_jay_g;
                break;
            case Con:
                resourceId = R.drawable.tag_con;
                break;
        }
        return resourceId;
    }
}
