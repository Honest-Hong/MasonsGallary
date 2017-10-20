package com.mason.kakao.masonsgallary.view;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.mason.kakao.masonsgallary.view.adapter.ImagesAdapter;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

public class ImagesActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private ImagesAdapter imagesAdapter;
    private ProgressBar progressBar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
    }

    @Override
    public void setupActivity() {
        setContentView(R.layout.activity_images);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imagesAdapter = new ImagesAdapter(this);
        recyclerView.setAdapter(imagesAdapter);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void requestPermission() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        loadImages();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();
                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void loadImages() {
        setLoadingIndicator(true);
        MasonApplication.get(this).getImagesRepository().getList()
                .subscribe(new SimpleObserver<List<ImageData>>() {
                    @Override
                    public void onNext(@NonNull List<ImageData> list) {
                        setLoadingIndicator(false);
                        imagesAdapter.setList(list);
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

    @Override
    public boolean onNavigationItemSelected(@android.support.annotation.NonNull MenuItem item) {
        Observable<List<ImageData>> observable = null;
        switch(item.getItemId()) {
            case R.id.menu_ryan:
                observable = MasonApplication.get(this).getImagesRepository().getTaggedList(Tag.Ryan);
                break;
            case R.id.menu_muzi:
                observable = MasonApplication.get(this).getImagesRepository().getTaggedList(Tag.Muzi);
                break;
            case R.id.menu_apeach:
                observable = MasonApplication.get(this).getImagesRepository().getTaggedList(Tag.Apeach);
                break;
            case R.id.menu_frodo:
                observable = MasonApplication.get(this).getImagesRepository().getTaggedList(Tag.Frodo);
                break;
            case R.id.menu_neo:
                observable = MasonApplication.get(this).getImagesRepository().getTaggedList(Tag.Neo);
                break;
            case R.id.menu_jay_g:
                observable = MasonApplication.get(this).getImagesRepository().getTaggedList(Tag.Jay_G);
                break;
            case R.id.menu_con:
                observable = MasonApplication.get(this).getImagesRepository().getTaggedList(Tag.Con);
                break;
        }
        if(observable == null) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        setLoadingIndicator(true);
        observable.subscribe(new SimpleObserver<List<ImageData>>() {
                     @Override
                     public void onNext(@NonNull List<ImageData> list) {
                         setLoadingIndicator(false);
                         imagesAdapter.setList(list);
                     }

                     @Override
                     public void onError(@NonNull Throwable e) {
                         setLoadingIndicator(false);
                     }
                 });
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
