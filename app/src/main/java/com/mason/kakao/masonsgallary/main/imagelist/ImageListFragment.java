package com.mason.kakao.masonsgallary.main.imagelist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.mason.kakao.masonsgallary.ExtraKeys;
import com.mason.kakao.masonsgallary.MasonApplication;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseFragment;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kakao on 2017. 10. 31..
 */

public class ImageListFragment extends BaseFragment implements ImageListContract.View, ImageListAdapter.ImageListItemEvent{
    private Context context;

    @Inject
    ImageListContract.Presenter presenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ImageListAdapter imageListAdapter;

    private MenuItem menuDelete;

    private Tag currentTag = Tag.All;

    private OnShowDetailListener onShowDetailListener;

    public interface OnShowDetailListener {
        void onShowDetail(ImageData imageData);
    }

    public static ImageListFragment newInstance(Tag tag) {
        ImageListFragment fragment = new ImageListFragment();
        Bundle args = new Bundle();
        args.putString(ExtraKeys.TAG, tag.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init() {
        this.context = getContext();
        MasonApplication.get(context)
                .getAppComponent()
                .plus(new ImageListModule())
                .inject(this);
        presenter.bindView(this);
        currentTag = Tag.valueOf(getArguments().getString(ExtraKeys.TAG));
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnShowDetailListener) {
            onShowDetailListener = (OnShowDetailListener) context;
        }
    }

    @Override
    public void setupView(View view) {
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        imageListAdapter = new ImageListAdapter(context, this);
        recyclerView.setAdapter(imageListAdapter);
        presenter.loadImages(currentTag);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_images;
    }

    @Override
    public void showImageList(List<ImageListData> list) {
        imageListAdapter.setList(list);
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(!show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onChangeChecked(ImageListData listData) {
        imageListAdapter.changeImageData(listData);
    }

    @Override
    public void showDeleteMenu(boolean show) {
        menuDelete.setVisible(show);
    }

    @Override
    public void onImageRemoved(ImageData imageData) {
        imageListAdapter.removeImageData(imageData);
    }

    @Override
    public void onImageTagChanged(ImageData imageData) {
        if(currentTag == Tag.All || currentTag == imageData.getTag()) {
            imageListAdapter.changeImageData(imageData);
        } else {
            imageListAdapter.removeImageData(imageData);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        recyclerView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_images, menu);
        menuDelete = menu.findItem(R.id.menu_delete);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_delete:
                presenter.removeCheckedImages();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onImageClick(ImageListData imageListData) {
        presenter.onImageClick(imageListData);
    }

    @Override
    public void onImageLongClick(ImageListData imageListData) {
        presenter.onImageLongClick(imageListData);
    }

    @Override
    public void onShowDetail(ImageListData listData) {
        onShowDetailListener.onShowDetail(listData.getImageData());
    }

    @Override
    public void onChangeTag(Tag tag) {
        currentTag = tag;
        presenter.loadImages(tag);
    }
}
