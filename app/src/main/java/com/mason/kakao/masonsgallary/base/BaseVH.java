package com.mason.kakao.masonsgallary.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kakao on 2017. 10. 20..
 */

public abstract class BaseVH<T> extends RecyclerView.ViewHolder {
    protected T data;
    public BaseVH(View itemView) {
        super(itemView);
    }

    public abstract void setupView(T data);
}
