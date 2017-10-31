package com.mason.kakao.masonsgallary.view.adapter.holder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseVH;
import com.mason.kakao.masonsgallary.dialog.SelectingTagDialog;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.util.TagUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kakao on 2017. 10. 20..
 * 이미지를 보여주는 뷰 홀더
 */

public class ImageVH extends BaseVH<ImageListData>{
    private Context context;
    private ImageView imageTag;
    private ImageView imageView;
    private TextView textName;
    private TextView textPath;
    private TextView textDate;
    private View viewDim;

    public ImageVH(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        imageTag = itemView.findViewById(R.id.image_tag);
        imageView = itemView.findViewById(R.id.image_view);
        textName = itemView.findViewById(R.id.text_name);
        textPath = itemView.findViewById(R.id.text_path);
        textDate = itemView.findViewById(R.id.text_date);
        viewDim = itemView.findViewById(R.id.view_dim);
    }

    @Override
    public void bindView(ImageListData data) {
        this.data = data;
        int drawableId = TagUtil.getDrawableResourceByTag(data.getImageData().getTag());
        // 태그가 없는 경우 아무런 표시도 하지 않는다
        if(drawableId == 0) {
            imageTag.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else {
            Picasso.with(context)
                    .load(drawableId)
                    .fit()
                    .into(imageTag);
        }
        Picasso.with(context)
                .load(new File(data.getImageData().getPath()))
                .fit()
                .centerCrop()
                .into(imageView);
        textName.setText(data.getImageData().getName());
        textPath.setText(data.getImageData().getPath());
        // 이미지 데이터에 저장된 시간은 초 단위이기 때문에 1000을 곱하여 포멧팅 해준다
        Date date = new Date(Long.parseLong(data.getImageData().getDate()) * 1000);
        textDate.setText(SimpleDateFormat.getInstance().format(date));
        // 체크 유무에 따라 Dim을 표시해준다
        viewDim.setVisibility(data.isChecked()
                ? View.VISIBLE
                : View.GONE);

    }
}
