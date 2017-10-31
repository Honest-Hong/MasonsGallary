package com.mason.kakao.masonsgallary.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.io.Serializable;

/**
 * Created by kakao on 2017. 10. 24..
 * 태그를 선택할 수 있는 다이얼로그
 */

public class SelectingTagDialog extends DialogFragment {
    /**
     * 다이얼로그 생성 정적 메소드
     * @param imageData 선택된 이미지 데이터
     * @param onSelectListener 선택 결과 리스너
     * @return 생성된 다이얼로그
     */
    public static SelectingTagDialog newInstance(ImageData imageData, OnSelectListener onSelectListener) {
        SelectingTagDialog dialog = new SelectingTagDialog();
        Bundle args = new Bundle();
        args.putParcelable(ImageData.class.getName(), imageData);
        args.putSerializable(OnSelectListener.class.getName(), onSelectListener);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * AlertDialog.Builder를 사용하여 다이얼로그를 생성한다.
     * 다이얼로그는 태그 목록을 포함하며 선택 결과를 onSelectListener가 전달한다
     * @param savedInstanceState ..
     * @return ..
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setItems(getContext().getResources().getStringArray(R.array.tags)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Tag tag = Tag.values()[which];
                                ImageData imageData = getArguments().getParcelable(ImageData.class.getName());
                                imageData.setTag(tag);
                                OnSelectListener onSelectListener = (OnSelectListener) getArguments().getSerializable(OnSelectListener.class.getName());
                                onSelectListener.onSelect(tag);
                            }
                        })
                .setTitle("태그를 선택해주세요")
                .create();
    }

    /**
     * 선택 결과 리스너
     */
    public interface OnSelectListener extends Serializable {
        void onSelect(Tag tag);
    }
}
