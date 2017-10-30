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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mason.kakao.masonsgallary.MasonApplication;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseActivity;
import com.mason.kakao.masonsgallary.dialog.SelectingTagDialog;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ImagesContract.View, ImagesAdapter.ItemChangeListener {
    private RecyclerView recyclerView;
    private ImagesAdapter imagesAdapter;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ImagesContract.Presenter presenter;
    private Tag filteredTag = Tag.All;
    private MenuItem menuDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new ImagesPresenter(this, MasonApplication.get(this).getImagesRepository());
        checkPermissions();
    }

    @Override
    public void setupActivity() {
        setContentView(R.layout.activity_images);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imagesAdapter = new ImagesAdapter(this, this);
        recyclerView.setAdapter(imagesAdapter);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        setupDrawer();
    }

    @Override
    public void showImages(List<ImageListData> list) {
        imagesAdapter.setList(list);
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
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
                presenter.removeCheckedImages();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {
        Tag tag = getTagByMenuId(item.getItemId());
        if(tag != filteredTag) {
            filteredTag = tag;
            presenter.loadImages(filteredTag);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onClick(ImageListData listData) {
        presenter.checkImageList(listData);
    }

    @Override
    public void onLongClick(final ImageListData listData) {
        SelectingTagDialog.newInstance(listData.getImageData(), new SelectingTagDialog.OnSelectListener() {
            @Override
            public void onSelect(Tag tag) {
                if(tag == filteredTag) {
                    return;
                }

                if(filteredTag == Tag.All) {
                    imagesAdapter.changeImageData(listData);
                    presenter.changeImageData(listData);
                } else {
                    imagesAdapter.removeImageData(listData);
                }
            }
        }).show(getSupportFragmentManager(), SelectingTagDialog.class.getName());
    }

    @Override
    public void showChecked(ImageListData listData) {
        imagesAdapter.changeImageData(listData);
    }

    @Override
    public void showDeleteMenu(boolean show) {
        menuDelete.setVisible(show);
    }

    @Override
    public void removeListItem(ImageListData listData) {
        imagesAdapter.removeImageData(listData);
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
                        presenter.loadImages(filteredTag);
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
