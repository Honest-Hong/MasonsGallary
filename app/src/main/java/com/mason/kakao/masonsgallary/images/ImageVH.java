package com.mason.kakao.masonsgallary.images;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseVH;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class ImageVH extends BaseVH<ImageData>{
    private Context context;
    private ImageView imageTag;
    private ImageView imageView;
    private TextView textName;
    private TextView textPath;

    public ImageVH(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        imageTag = itemView.findViewById(R.id.image_tag);
        imageView = itemView.findViewById(R.id.image_view);
        textName = itemView.findViewById(R.id.text_name);
        textPath = itemView.findViewById(R.id.text_path);
    }

    @Override
    public void setupView(ImageData data) {
        this.data = data;
        int drawableId = 0;
        switch(data.getTag()) {
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
        if(drawableId == 0) {
            imageTag.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else {
            Picasso.with(context)
                    .load(drawableId)
                    .fit()
                    .into(imageTag);
        }
        Picasso.with(context)
                .load(new File(data.getPath()))
                .fit()
                .centerCrop()
                .into(imageView);
        textName.setText(data.getName());
        textPath.setText(data.getPath());
    }
}
