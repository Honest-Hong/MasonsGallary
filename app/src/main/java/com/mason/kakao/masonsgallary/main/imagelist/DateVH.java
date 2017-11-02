package com.mason.kakao.masonsgallary.main.imagelist;

import android.view.View;
import android.widget.TextView;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseVH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kakao on 2017. 11. 1..
 */

public class DateVH extends BaseVH {
    @BindView(R.id.text_date)
    TextView textDate;

    public DateVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setupView(Object data) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        textDate.setText(dateFormat.format(new Date((long)data)));
    }
}
