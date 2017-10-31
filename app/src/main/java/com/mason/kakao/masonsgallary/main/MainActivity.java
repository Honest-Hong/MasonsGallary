package com.mason.kakao.masonsgallary.main;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseActivity;
import com.mason.kakao.masonsgallary.main.detail.ImageDetailFragment;
import com.mason.kakao.masonsgallary.main.imagelist.ImageListContract;
import com.mason.kakao.masonsgallary.main.imagelist.ImageListFragment;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;
import com.mason.kakao.masonsgallary.util.TagUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ImageListFragment.OnShowDetailListener, ImageDetailFragment.ImageDataChangeListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ImageListContract.View imageListView;
    private final Object fence = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();
    }

    @Override
    public void setupActivity() {
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setupDrawer();

        ImageListFragment fragment = ImageListFragment.newInstance(Tag.All);
        imageListView = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment, ImageListFragment.class.getName())
                .commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if(fragment instanceof ImageDetailFragment) {
            hideDetail();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {
        Tag tag = TagUtil.getTagByMenu(item);
        changeTag(tag);

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onShowDetail(ImageData imageData) {
        showDetail(imageData);
    }

    @Override
    public void onImageRemoved(ImageData imageData) {
        hideDetail();
        imageListView.onImageRemoved(imageData);
    }

    @Override
    public void onImageTagChange(ImageData imageData) {
        imageListView.onImageTagChanged(imageData);
    }

    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void checkPermissions() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        changeTag(Tag.All);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();
                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void changeTag(Tag tag) {
        imageListView.onChangeTag(tag);
    }

    private void showDetail(ImageData imageData) {
        synchronized (fence) {
            hideDetail();
            imageListView.setVisible(false);
            drawerToggle.setDrawerIndicatorEnabled(false);
            ImageDetailFragment fragment = ImageDetailFragment.newInstance(imageData);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_layout, fragment, ImageDetailFragment.class.getName())
                    .commit();
        }
    }

    private void hideDetail() {
        synchronized (fence) {
            imageListView.setVisible(true);
            drawerToggle.setDrawerIndicatorEnabled(true);
            ImageDetailFragment fragment = (ImageDetailFragment)getSupportFragmentManager().findFragmentByTag(ImageDetailFragment.class.getName());
            if(fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }
    }
}
