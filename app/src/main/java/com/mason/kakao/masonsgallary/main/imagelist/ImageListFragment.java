package com.mason.kakao.masonsgallary.main.imagelist;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mason.kakao.masonsgallary.ExtraKeys;
import com.mason.kakao.masonsgallary.MasonApplication;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseFragment;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kakao on 2017. 10. 31..
 */

public class ImageListFragment extends BaseFragment implements ImageListContract.View {
    private Context context;

    @Inject
    ImageListContract.Presenter presenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ImageListAdapter imageListAdapter;

    private MenuItem menuDelete;

    private MenuItem menuLayout;

    private Tag currentTag = Tag.All;

    private OnShowDetailListener onShowDetailListener;

    private boolean showList = false;

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
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                if(showList) {
                    if(position > 0) {
                        outRect.top = 3;
                    }
                } else {
                    if(position > 2) {
                        outRect.top = 3;
                    }
                    if(position % 3 != 0) {
                        outRect.left = 3;
                    }
                }
            }
        });
        imageListAdapter = new ImageListAdapter();
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
    public boolean onBackPressed() {
        if(menuDelete.isVisible()) {
            presenter.cancelChecked();
            return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_images, menu);
        menuDelete = menu.findItem(R.id.menu_delete);
        menuLayout = menu.findItem(R.id.menu_layout);
        menuLayout.setIcon(showList
                ? R.drawable.ic_view_list_black_24dp
                : R.drawable.ic_grid_on_black_24dp);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_delete:
                presenter.removeCheckedImages();
                break;
            case R.id.menu_layout:
                toggleLayout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowDetail(ImageListData listData) {
        onShowDetailListener.onShowDetail(listData.getImageData());
    }

    @Override
    public void onChangeTag(Tag tag) {
        if(tag != currentTag) {
            currentTag = tag;
            presenter.loadImages(tag);
        }
    }

    private void toggleLayout() {
        showList = !showList;
        menuLayout.setIcon(showList
                ? R.drawable.ic_view_list_black_24dp
                : R.drawable.ic_grid_on_black_24dp);
        recyclerView.setLayoutManager(showList
                ? new LinearLayoutManager(context)
                : new GridLayoutManager(context, 3));
        recyclerView.setAdapter(imageListAdapter);
    }

    public class ImageListAdapter extends RecyclerView.Adapter<ImageVH> {

        private List<ImageListData> list;

        public ImageListAdapter() {
            this.list = Collections.emptyList();
        }

        public void setList(List<ImageListData> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
            final ImageVH holder =
                    new ImageVH(LayoutInflater.from(context).inflate(showList
                                    ? R.layout.item_image_list
                                    : R.layout.item_image_grid
                            , parent, false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    presenter.onImageClick(list.get(position));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getAdapterPosition();
                    presenter.onImageLongClick(list.get(position));
                    return false;
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(final ImageVH holder, int position) {
            holder.setupView(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void changeImageData(ImageListData imageData) {
            int index = list.indexOf(imageData);
            notifyItemChanged(index);
        }

        public void changeImageData(ImageData imageData) {
            int index = -1;
            for(int i=0; i<list.size(); i++) {
                if(imageData.getPath().equals(list.get(i).getImageData().getPath())) {
                    index = i;
                    break;
                }
            }
            list.get(index).updateImageData(imageData);
            notifyItemChanged(index);
        }

        public void removeImageData(ImageData imageData) {
            int index = -1;
            for(int i=0; i<list.size(); i++) {
                if(imageData.getPath().equals(list.get(i).getImageData().getPath())) {
                    index = i;
                    break;
                }
            }
            list.remove(index);
            notifyItemRemoved(index);
        }
    }
}
