package com.mason.kakao.masonsgallary.images;

import android.Manifest;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mason.kakao.masonsgallary.MasonApplication;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseActivity;
import com.mason.kakao.masonsgallary.databinding.ActivityImagesBinding;
import com.mason.kakao.masonsgallary.images.adapter.ImagesAdapter;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;

public class ImagesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityImagesBinding mBinding;
    private ActionBarDrawerToggle mDrawerToggle;

    private ImagesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    @Override
    public void setupActivity() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_images);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(new ImagesAdapter(this));
        mBinding.navigation.setNavigationItemSelectedListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, R.string.drawer_open, R.string.drawer_close);
        mBinding.drawerLayout.addDrawerListener(mDrawerToggle);

        mViewModel = new ImagesViewModel(MasonApplication.get(this).getImagesRepository());
        mBinding.setViewModel(mViewModel);
        mBinding.setList(mViewModel.getList());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {
        Tag tag = getTagByMenuId(item.getItemId());
        if(tag != null) {
            mViewModel.loadList(tag);
        }

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void checkPermissions() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mViewModel.loadList(Tag.All);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();
                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private Tag getTagByMenuId(int id) {
        Tag tag = null;
        switch(id) {
            case R.id.menu_ryan:
                tag = Tag.Ryan;
                break;
            case R.id.menu_muzi:
                tag = Tag.Muzi;
                break;
            case R.id.menu_apeach:
                tag = Tag.Apeach;
                break;
            case R.id.menu_frodo:
                tag = Tag.Frodo;
                break;
            case R.id.menu_neo:
                tag = Tag.Neo;
                break;
            case R.id.menu_jay_g:
                tag = Tag.Jay_G;
                break;
            case R.id.menu_con:
                tag = Tag.Con;
                break;
        }
        return tag;
    }
}
