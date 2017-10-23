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
import com.mason.kakao.masonsgallary.base.SimpleObserver;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

public class ImagesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ImagesContracter.View {
    private RecyclerView mRecyclerView;
    private ImagesAdapter mImagesAdapter;
    private ProgressBar mProgressBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ImagesContracter.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new ImagesPresenter(this, MasonApplication.get(this).getImagesRepository());
        checkPermissions();
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
        Tag tag = null;
        switch(item.getItemId()) {
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

        if(tag != null) {
            mPresenter.loadTaggedImages(tag);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void setupActivity() {
        setContentView(R.layout.activity_images);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mImagesAdapter = new ImagesAdapter(this);
        mRecyclerView.setAdapter(mImagesAdapter);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        setupDrawer();
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void checkPermissions() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mPresenter.loadImages();
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
