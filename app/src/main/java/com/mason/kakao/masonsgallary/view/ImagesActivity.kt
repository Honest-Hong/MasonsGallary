package com.mason.kakao.masonsgallary.view

import android.Manifest
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mason.kakao.masonsgallary.MasonApplication
import com.mason.kakao.masonsgallary.R
import com.mason.kakao.masonsgallary.base.BaseActivity
import com.mason.kakao.masonsgallary.base.SimpleObserver
import com.mason.kakao.masonsgallary.model.ImagesRepository
import com.mason.kakao.masonsgallary.model.data.ImageData
import com.mason.kakao.masonsgallary.model.data.ImageListData
import com.mason.kakao.masonsgallary.model.data.Tag
import com.mason.kakao.masonsgallary.util.TagUtil
import com.mason.kakao.masonsgallary.view.adapter.ImagesAdapter
import kotlinx.android.synthetic.main.activity_images.*
import java.util.ArrayList

/**
 * Created by kakao on 2017. 10. 31..
 */

class ImagesActivity : BaseActivity(), ImagesAdapter.ItemEventListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var repository : ImagesRepository
    private lateinit var imagesAdapter : ImagesAdapter
    private lateinit var drawerToggle : ActionBarDrawerToggle
    private val checkedList : MutableList<ImageData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = MasonApplication.get(baseContext).imagesRepository
        checkPermissions()
    }

    override fun setupActivity() {
        setContentView(R.layout.activity_images)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        imagesAdapter = ImagesAdapter(this, this)
        recycler_view.adapter = imagesAdapter

        navigation.setNavigationItemSelectedListener(this)
        drawerToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.drawer_open, R.string.drawer_close)
        drawer_layout.addDrawerListener(drawerToggle)
    }

    override fun onClick(listData: ImageListData) {
        val isChecked = listData.isChecked.not()
        listData.isChecked = isChecked
        if(isChecked) checkedList.add(listData.imageData)
        else checkedList.remove(listData.imageData)
        imagesAdapter.changeImageData(listData)
    }

    override fun onLongClick(imageData: ImageListData?) {
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val tag = TagUtil.getTagById(item.itemId)
        loadImages(tag)
        return false
    }

    private fun setLoadingIndicator(show : Boolean) {
        recycler_view.visibility =
                if(show) View.GONE
                else View.VISIBLE
        progress_bar.visibility =
                if(show) View.VISIBLE
                else View.GONE
    }

    private fun loadImages(tag : Tag) {
        setLoadingIndicator(true)
        repository.getList(tag)
                .subscribe(object : SimpleObserver<List<ImageData>>() {
                    override fun onNext(t: List<ImageData>) {
                        setLoadingIndicator(false)
                        imagesAdapter.setList(t)
                    }

                    override fun onError(e: Throwable) {
                        setLoadingIndicator(false)
                    }
                })
    }

    private fun checkPermissions() {
        TedPermission.with(this)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        loadImages(Tag.All)
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                        finish()
                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
    }
}