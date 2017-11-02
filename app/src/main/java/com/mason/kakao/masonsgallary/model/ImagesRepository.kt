package com.mason.kakao.masonsgallary.model

import android.content.Context
import android.provider.MediaStore
import com.mason.kakao.masonsgallary.model.data.ImageData
import com.mason.kakao.masonsgallary.model.data.Tag
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Created by kakao on 2017. 11. 2..
 */

class ImagesRepository(
    private val context : Context
) {
    private var list: List<ImageData> = Collections.emptyList()

    fun getList(tag: Tag): Observable<List<ImageData>> =
        Observable.create(ObservableOnSubscribe<List<ImageData>> { e ->
            if(tag == Tag.All) {
                e.onNext(searchImageData())
            } else {
                if(list.isEmpty()) {
                    searchImageData()
                }
                e.onNext(retrieveImages(tag))
            }
        }).delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())

    fun removeImageData(imageData: ImageData) {

    }

    private fun searchImageData() : List<ImageData> {
        if(list.isNotEmpty()) {
            return list
        }

        val projection = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_MODIFIED)
        val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null)
        val result = ArrayList<ImageData>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val arr = Tag.values()
                val random = Random()
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
                do {
                    val data = cursor.getString(dataColumn)
                    val name = cursor.getString(nameColumn)
                    val date = cursor.getString(dateColumn)
                    val index = Math.abs(random.nextInt()) % (arr.size - 1)
                    result.add(ImageData(data, name, arr[index], date))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        this.list = result
        return result
    }

    private fun retrieveImages(tag: Tag) : List<ImageData> =
            list.filter { imageData ->
                    imageData.tag == tag
            }
}