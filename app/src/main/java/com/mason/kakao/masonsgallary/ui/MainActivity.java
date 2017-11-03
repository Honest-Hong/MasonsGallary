package com.mason.kakao.masonsgallary.ui;

import android.Manifest;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseActivity;
import com.mason.kakao.masonsgallary.databinding.ActivityMainBinding;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.ui.detail.ImageDetailFragment;
import com.mason.kakao.masonsgallary.ui.detail.OnImageDeleteListener;
import com.mason.kakao.masonsgallary.ui.images.ImageListFragment;
import com.mason.kakao.masonsgallary.model.data.Tag;
import com.mason.kakao.masonsgallary.ui.images.adapter.OnShowDetailListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnShowDetailListener, OnImageDeleteListener {
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem menuDelete;
    private ImageListFragment listFragment = ImageListFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        binding.navigation.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawer_open, R.string.drawer_close);
        binding.drawerLayout.addDrawerListener(drawerToggle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, listFragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {
        Tag tag = getTagByMenuId(item.getItemId());
//        viewModel.changeFilter(tag);

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return false;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_images, menu);
        menuDelete = menu.findItem(R.id.menu_delete);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()) {
            case R.id.menu_delete:
//                viewModel.removeCheckedList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(hideDetail()) {
            // Do nothing
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onShowDetail(@NotNull ImageData imageData) {
        showDetail(imageData);
    }

    @Override
    public void onDelete(@NotNull ImageData imageData) {
        hideDetail();
        listFragment.onDelete(imageData);
    }

    private void showDetail(ImageData imageData) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(listFragment)
                .add(R.id.frameLayout, ImageDetailFragment.newInstance(imageData), ImageDetailFragment.class.getName())
                .commit();
    }

    private boolean hideDetail() {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(ImageDetailFragment.class.getName());
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .show(listFragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }

    private Tag getTagByMenuId(int id) {
        Tag tag = null;
        switch(id) {
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
}
