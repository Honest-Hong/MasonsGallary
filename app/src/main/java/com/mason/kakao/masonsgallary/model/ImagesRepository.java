package com.mason.kakao.masonsgallary.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kakao on 2017. 10. 20..
 * 이미지 데이터 저장소 모델
 * 내부 저장소로부터 모든 이미지를 불러오고 원하는 태그로 이미지를 불러올 수 있다
 */

public class ImagesRepository {
    // 내부 저장소 접근을 위한 Context
    private Context context;
    // 이미지 데이터 목록
    private List<ImageData> list = null;

    public ImagesRepository(Context context) {
        this.context = context;
    }

    /**
     * 이미지를 불러오는 메소드
     * @param tag 원하는 태그
     * @return 이미지 목록이 담긴 Observable
     */
    public Observable<List<ImageData>> getList(final Tag tag) {
        return Observable.create(new ObservableOnSubscribe<List<ImageData>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<ImageData>> e) throws Exception {
                        if(tag == Tag.All) {
                            e.onNext(searchImageData());
                        } else {
                            if(list == null) {
                                searchImageData();
                            }
                            e.onNext(retrieveImages(tag));
                        }
                    }
                })
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 이미지 데이터 변환 메소드
     * 내부 데이터베이스를 가지고있지 않기 때문에 아무 행동도 하지 않는다
     * @param imageData 변환된 이미지 데이터
     */
    public void changeImageData(ImageData imageData) {
    }

    /**
     * 이미지 데이터 삭제 메소드
     * 캐시하고있던 목록에서도 삭제시켜준다
     * @param imageData 삭제할 데이터
     */
    public void removeImageData(ImageData imageData) {
        if(list != null) {
            list.remove(imageData);
        }
    }

    /**
     * 내부 저장소로부터 모든 이미지 데이터를 불러오는 메소드
     * @return 모든 이미지 데이터 목록
     */
    private List<ImageData> searchImageData() {
        if(list != null) {
            return list;
        }

        final String[] projection = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_MODIFIED
        };
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        List<ImageData> result = new ArrayList<>();
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                Tag[] arr = Tag.values();
                Random random = new Random();
                final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                final int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                final int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
                do {
                    String data = cursor.getString(dataColumn);
                    String name = cursor.getString(nameColumn);
                    String date = cursor.getString(dateColumn);
                    int index = Math.abs(random.nextInt()) % (arr.length - 1);
                    result.add(new ImageData(data, name, arr[index], date));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        this.list = result;
        return result;
    }

    /**
     * 모든 이미지 목록에서 원하는 태그로 새로운 목록을 가져오는 메소드
     * @param tag 원하는 태그
     * @return 새로운 이미지 목록
     */
    private List<ImageData> retrieveImages(Tag tag) {
        if(list == null) {
            return Collections.emptyList();
        }

        List<ImageData> list = new ArrayList<>();
        for(ImageData imageData : this.list) {
            if(imageData.getTag() == tag) {
                list.add(imageData);
            }
        }

        return list;
    }
}
