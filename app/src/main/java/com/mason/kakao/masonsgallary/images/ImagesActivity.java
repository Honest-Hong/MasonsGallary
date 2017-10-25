package com.mason.kakao.masonsgallary.images;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mason.kakao.masonsgallary.MasonApplication;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseActivity;
import com.mason.kakao.masonsgallary.dialog.SelectingTagDialog;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ImagesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ImagesContracter.View, TagChangeListener {
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Inject ImagesAdapter mImagesAdapter;
    @Inject ImagesContracter.Presenter mPresenter;

    private Tag mFilteredTag = Tag.All;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    @Override
    public void setupActivity() {
        setContentView(R.layout.activity_images);

        MasonApplication.get(this)
                .getAppComponent()
                .plus(new ImagesActivityModule(this))
                .inject(this);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mImagesAdapter);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        setupDrawer();
    }

    @Override
    public void showImages(List<ImageData> list) {
        mImagesAdapter.setList(list);
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        mRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
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
        if(tag != mFilteredTag) {
            mFilteredTag = tag;
            mPresenter.loadImages(mFilteredTag);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void selectTag(final ImageData imageData) {
        SelectingTagDialog.newInstance(imageData, new SelectingTagDialog.OnSelectListener() {
            @Override
            public void onSelect(Tag tag) {
                if(tag == mFilteredTag) {
                    return;
                }

                if(mFilteredTag == Tag.All) {
                    mImagesAdapter.changeImageData(imageData);
                    mPresenter.changeImageData(imageData);
                } else {
                    mImagesAdapter.removeImageData(imageData);
                }
            }
        }).show(getSupportFragmentManager(), SelectingTagDialog.class.getName());
    }

    private Tag getTagByMenuId(int id) {
        Tag tag = null;
        switch (id) {
            case R.id.menu_all:
                tag = Tag.All;
                break;
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
            case R.id.menu_tube:
                tag = Tag.Tube;
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

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void checkPermissions() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mPresenter.loadImages(mFilteredTag);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();
                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
}
