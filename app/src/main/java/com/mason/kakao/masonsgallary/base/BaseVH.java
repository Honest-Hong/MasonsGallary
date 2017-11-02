package com.mason.kakao.masonsgallary.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kakao on 2017. 10. 20..
 */

public abstract class BaseVH extends RecyclerView.ViewHolder {
    protected Object data;
    public BaseVH(View itemView) {
        super(itemView);
    }

    public Object getData() {
        return data;
    }

    public abstract void setupView(Object data);
}
