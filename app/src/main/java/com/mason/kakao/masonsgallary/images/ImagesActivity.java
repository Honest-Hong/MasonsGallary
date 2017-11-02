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
import android.view.Menu;
import android.view.MenuItem;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mason.kakao.masonsgallary.MasonApplication;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseActivity;
import com.mason.kakao.masonsgallary.databinding.ActivityImagesBinding;
import com.mason.kakao.masonsgallary.images.adapter.ImagesAdapter;
import com.mason.kakao.masonsgallary.images.viewmodel.ImagesViewModel;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;

public class ImagesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImagesViewModel viewModel;
    private ActivityImagesBinding binding;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem menuDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    @Override
    public void setupActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_images);
        viewModel = new ImagesViewModel(this, MasonApplication.get(this).getImagesRepository());
        binding.setViewModel(viewModel);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new ImagesAdapter(this, viewModel));
        binding.navigation.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawer_open, R.string.drawer_close);
        binding.drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {
        Tag tag = getTagByMenuId(item.getItemId());
        viewModel.changeFilter(tag);

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
                viewModel.removeCheckedList();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void checkPermissions() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        viewModel.loadFirst();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
}
