package com.mason.kakao.masonsgallary.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kakao on 2017. 10. 20..
 * 기본 뷰 홀더 로직을 가지고 있는 클래스
 * 뷰 홀더에서 보여주고있는 데이터를 가지고 있으며
 * bindView를 통해 뷰를 최신화 한다.
 */

public abstract class BaseVH<T> extends RecyclerView.ViewHolder {
    // 현재 보여지고 있는 데이터
    protected T data;
    // 기본 생성자
    public BaseVH(View itemView) {
        super(itemView);
    }
    // 뷰 최신화 작업
    public abstract void bindView(T data);
}
