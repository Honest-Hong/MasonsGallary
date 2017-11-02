package com.mason.kakao.masonsgallary.util;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.model.data.Tag;

/**
 * Created by kakao on 2017. 11. 2..
 */

public class TagUtil {
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
