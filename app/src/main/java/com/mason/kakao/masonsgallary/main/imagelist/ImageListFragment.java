package com.mason.kakao.masonsgallary.main.imagelist;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.mason.kakao.masonsgallary.base.BaseVH;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private OnImageListEventListener onImageListEventListener;

    private boolean showList = false;

    public interface OnImageListEventListener {
        void onShowDetail(ImageData imageData);
        void onChangeCheckMode(boolean isCheckMode);
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
        if(context instanceof OnImageListEventListener) {
            onImageListEventListener = (OnImageListEventListener) context;
        }
    }

    @Override
    public void setupView(View view) {
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setItemAnimator(null);
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
        onImageListEventListener.onChangeCheckMode(show);
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
        onImageListEventListener.onShowDetail(listData.getImageData());
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

    public class ImageListAdapter extends RecyclerView.Adapter<BaseVH> {
        private Map<Long, List<ImageListData>> listMap;

        public ImageListAdapter() {
            this.listMap = new LinkedHashMap<>();
        }

        public void setList(List<ImageListData> list) {
            listMap.clear();
            for(ImageListData data : list) {
                long date = Long.parseLong(data.getImageData().getDate()) / (60 * 60 * 24);
                if(!listMap.containsKey(date)) {
                    listMap.put(date , new ArrayList<ImageListData>());
                }
                listMap.get(date).add(data);
            }
            notifyDataSetChanged();
        }

        @Override
        public BaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 0) {
                return new DateVH(LayoutInflater.from(context).inflate(R.layout.item_date, parent, false));
            } else {
                final ImageVH holder =
                        new ImageVH(LayoutInflater.from(context).inflate(showList
                                        ? R.layout.item_image_list
                                        : R.layout.item_image_grid
                                , parent, false));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onImageClick((ImageListData) holder.getData());
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        presenter.onImageLongClick((ImageListData) holder.getData());
                        return false;
                    }
                });
                return holder;
            }
        }

        @Override
        public void onBindViewHolder(final BaseVH holder, int position) {
            if(holder.getItemViewType() == 0) {
                String date = findDataByPosition(position).getImageData().getDate();
                holder.setupView(Long.parseLong(date) * 1000);
            } else {
                Log.d("onBindViewHolder", findDataByPosition(position).toString());
                Log.d("onBindViewHolder", "position: " + position);
                holder.setupView(findDataByPosition(position));
            }
        }

        @Override
        public int getItemCount() {
            int total = 0;
            for(Map.Entry<Long, List<ImageListData>> entry : listMap.entrySet()) {
                total += entry.getValue().size() + (showList ? 1 : 0);
            }
            return total;
//            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(showList) {
                int cursor = 0;
                for(Map.Entry<Long, List<ImageListData>> entry : listMap.entrySet()) {
                    if(position < cursor + entry.getValue().size() + 1) {
                        if(showList && position == cursor) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                    cursor += entry.getValue().size() + (showList ? 1 : 0);
                }
                throw new NullPointerException("Invalid position: " + position);
            } else {
                return 1;
            }
        }

        public void changeImageData(ImageListData imageData) {
            Log.d("changeImageData", imageData.toString());
            int index = findPositionByImageData(imageData.getImageData());
            Log.d("changeImageData", "index: " + index);
            notifyItemChanged(index);
        }

        public void changeImageData(ImageData imageData) {
            Log.d("changeImageData", imageData.toString());
            int index = findPositionByImageData(imageData);
            Log.d("changeImageData", "index: " + index);
            notifyItemChanged(index);
        }

        public void removeImageData(ImageData imageData) {
            int position = 0;
            for(Map.Entry<Long, List<ImageListData>> entry : listMap.entrySet()) {
                if(showList) {
                    position++;
                }
                boolean removed = false;
                for(ImageListData data : entry.getValue()) {
                    if(data.getImageData().getPath().equals(imageData.getPath())) {
                        entry.getValue().remove(data);
                        removed = true;
                        break;
                    }
                    position++;
                }
                if(removed) {
                    notifyItemRemoved(position);
                    if(entry.getValue().size() == 0) {
                        notifyItemRemoved(position - 1);
                        listMap.remove(entry.getKey());
                    }
                    break;
                }
            }
        }

        private ImageListData findDataByPosition(int position) {
            int cursor = 0;
            for(Map.Entry<Long, List<ImageListData>> entry : listMap.entrySet()) {
                if(position < cursor + entry.getValue().size() + (showList ? 1 : 0)) {
                    if(showList && position == cursor) {
                        return entry.getValue().get(0);
                    } else {
                        return entry.getValue().get(position - cursor - (showList ? 1 : 0));
                    }
                }
                cursor += entry.getValue().size() + (showList ? 1 : 0);
            }
            throw new NullPointerException("Invalid position: " + position);
        }

        private int findPositionByImageData(ImageData imageData) {
            int position = 0;
            for(Map.Entry<Long, List<ImageListData>> entry : listMap.entrySet()) {
                if(showList) {
                    position++;
                }
                for(ImageListData data : entry.getValue()) {
                    if(data.getImageData().getPath().equals(imageData.getPath())) {
                        return position;
                    }
                    position++;
                }
            }
            return -1;
        }
    }
}
