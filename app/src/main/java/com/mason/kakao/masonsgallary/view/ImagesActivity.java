package com.mason.kakao.masonsgallary.view;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mason.kakao.masonsgallary.MasonApplication;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseActivity;
import com.mason.kakao.masonsgallary.base.SimpleObserver;
import com.mason.kakao.masonsgallary.dialog.SelectingTagDialog;
import com.mason.kakao.masonsgallary.model.ImagesRepository;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;
import com.mason.kakao.masonsgallary.util.TagUtil;
import com.mason.kakao.masonsgallary.view.adapter.ImagesAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class ImagesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ImagesAdapter.ItemEventListener, ImageDetailFragment.ImageDetailEvents {
    // 이미지 리스트 뷰
    private RecyclerView recyclerView;
    // 이미지 리스트 어뎁터
    private ImagesAdapter imagesAdapter;
    // 데이터 로딩 지시자
    private ProgressBar progressBar;
    // 네비게이션 메뉴
    private NavigationView navigationView;
    // 드로우어
    private DrawerLayout drawerLayout;
    // 드로우어 토글 버튼
    private ActionBarDrawerToggle drawerToggle;
    // 현재 필터
    private Tag filterTag = Tag.All;
    // 이미지 목록 저장소
    private ImagesRepository repository;
    // 체크된 데이터 목록
    private List<ImageListData> checkedListData = new ArrayList<>();
    // 삭제 메뉴 버튼
    private MenuItem menuDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = MasonApplication.get(this).getImagesRepository();
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
                removeCheckedList();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {
        Tag tag = TagUtil.getTagByMenu(item);
        if(tag != filterTag) {
            filterTag = tag;
            loadImages(filterTag);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    /**
     * 이미지 선택 이벤트 처리 메소드
     * 이미지를 체크하거나 해제한다
     * @param listData 선택한 이미지
     */
    @Override
    public void onClick(ImageListData listData) {
        if(checkedListData.size() > 0) {
            checkListItem(listData);
        } else {
            showDetailView(listData.getImageData());
        }
    }

    /**
     * 이미지 길게 선택 이벤트 처리 메소드
     * 이미지의 태그를 변경할 수 있도록 해준다
     * @param listData 선택한 이미지
     */
    @Override
    public void onLongClick(final ImageListData listData) {
        if(checkedListData.size() > 0) {
            SelectingTagDialog.newInstance(listData.getImageData(), new SelectingTagDialog.OnSelectListener() {
                @Override
                public void onSelect(Tag tag) {
                    if(tag == filterTag) {
                        return;
                    }

                    if(filterTag == Tag.All) {
                        imagesAdapter.changeImageData(listData);
                        repository.changeImageData(listData.getImageData());
                    } else {
                        imagesAdapter.removeImageData(listData);
                    }
                }
            }).show(getSupportFragmentManager(), SelectingTagDialog.class.getName());
        } else {
            checkListItem(listData);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment imageDetailFragment = getSupportFragmentManager().findFragmentByTag(ImageDetailFragment.class.getName());
        if(imageDetailFragment != null && imageDetailFragment.isVisible()) {
            hideDetailView();
        } else if(checkedListData.size() > 0) {
            cancelCheckedList();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void doDeleteFromDetail(ImageData imageData) {
        hideDetailView();
        imagesAdapter.removeImageData(imageData);
        repository.removeImageData(imageData);
    }

    @Override
    public void changeTagFromDetail(ImageData imageData) {
        if(filterTag == Tag.All || filterTag == imageData.getTag()) {
            imagesAdapter.changeImageData(imageData);
        } else {
            imagesAdapter.removeImageData(imageData);
        }
        repository.changeImageData(imageData);
    }

    private void loadImages(Tag tag) {
        setLoadingIndicator(true);
        repository.getList(tag)
                .subscribe(new SimpleObserver<List<ImageData>>() {
                    @Override
                    public void onNext(@NonNull List<ImageData> list) {
                        setLoadingIndicator(false);
                        imagesAdapter.setList(list);
                        checkedListData = new ArrayList<>();
                        menuDelete.setVisible(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        setLoadingIndicator(false);
                    }
                });
    }

    private void setLoadingIndicator(boolean load) {
        recyclerView.setVisibility(load ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(load ? View.VISIBLE : View.GONE);
    }

    private void setupDrawer() {
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
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
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void checkPermissions() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        loadImages(filterTag);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();
                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    /**
     * 체크한 이미지 목록을 삭제하는 메소드
     */
    private void removeCheckedList() {
        Iterator<ImageListData> iterator = checkedListData.iterator();
        while(iterator.hasNext()) {
            ImageListData listData = iterator.next();
            imagesAdapter.removeImageData(listData);
            repository.removeImageData(listData.getImageData());
            iterator.remove();
        }
        menuDelete.setVisible(false);
    }

    /**
     * 체크 상태를 해제하는 메소드
     */
    private void cancelCheckedList() {
        Iterator<ImageListData> iterator = checkedListData.iterator();
        while(iterator.hasNext()) {
            ImageListData listData = iterator.next();
            listData.setChecked(false);
            imagesAdapter.changeImageData(listData);
            iterator.remove();
        }
        menuDelete.setVisible(false);
    }

    private void showDetailView(ImageData imageData) {
        drawerToggle.setDrawerIndicatorEnabled(false);
        recyclerView.setVisibility(View.GONE);
        Fragment fragment = ImageDetailFragment.newInstance(imageData);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, fragment, ImageDetailFragment.class.getName())
                .commit();
    }

    private void hideDetailView() {
        drawerToggle.setDrawerIndicatorEnabled(true);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ImageDetailFragment.class.getName());
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void checkListItem(ImageListData listData) {
        boolean isChecked = !listData.isChecked();
        listData.setChecked(isChecked);
        imagesAdapter.changeImageData(listData);
        if(isChecked) {
            checkedListData.add(listData);
        } else {
            checkedListData.remove(listData);
        }
        menuDelete.setVisible(checkedListData.size() > 0);
    }
}
