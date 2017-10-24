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
 */

public class SelectingTagDialog extends DialogFragment {
    public static SelectingTagDialog newInstance(ImageData imageData, OnSelectListener onSelectListener) {
        SelectingTagDialog dialog = new SelectingTagDialog();
        Bundle args = new Bundle();
        args.putParcelable(ImageData.class.getName(), imageData);
        args.putSerializable(OnSelectListener.class.getName(), onSelectListener);
        dialog.setArguments(args);
        return dialog;
    }

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
                .setTitle(R.string.select_tag_please)
                .create();
    }

    public interface OnSelectListener extends Serializable {
        void onSelect(Tag tag);
    }
}
