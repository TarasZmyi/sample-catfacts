package tz.app.sample.catfacts.presentation.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tz.app.sample.catfacts.R;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

public final class CatItemAdapter extends RecyclerView.Adapter<CatItemViewHolder> {

    private final String TAG;
    private final ILogger logger = LogManager.getLogger();

    private final List<CatItemDataHolder> catItemDataHolders = new ArrayList<>();

    public CatItemAdapter() {
        TAG = LogManager.logObjCreation(this);
        setHasStableIds(true);
    }

    public void updateData(final List<CatItemDataHolder> catItemData) {
        logger.debug(TAG, "updateData, size = " + catItemData.size());

        final DiffUtilCallback<CatItemDataHolder> diffCallback = new DiffUtilCallback<>(catItemDataHolders, catItemData);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, false);

        catItemDataHolders.clear();
        catItemDataHolders.addAll(catItemData);

        diffResult.dispatchUpdatesTo(CatItemAdapter.this);
    }

    public void clearData() {
        logger.debug(TAG, "clearData");
        catItemDataHolders.clear();
        notifyDataSetChanged();
    }

    @Override
    public final CatItemViewHolder onCreateViewHolder(final ViewGroup parent, final @LayoutRes int viewType) {
        logger.debug(TAG, "onCreateViewHolder");
        final Context context = parent.getContext();
        final View root = LayoutInflater.from(context).inflate(viewType, parent, false);
        return new CatItemViewHolder(root);
    }

    @Override
    public final void onBindViewHolder(@NonNull final CatItemViewHolder holder, int position) {
        logger.debug(TAG, "onBindViewHolder, pos = " + position);
        final CatItemDataHolder catItemDataHolder = catItemDataHolders.get(position);
        holder.bindData(catItemDataHolder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final CatItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.attached();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull final CatItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.detached();
    }

    @Override
    public void onViewRecycled(@NonNull final CatItemViewHolder holder) {
        super.onViewRecycled(holder);
        logger.debug(TAG, "onViewRecycled");
        holder.unbindData();

    }

    @Override
    public long getItemId(int position) {
        return catItemDataHolders.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return catItemDataHolders.size();
    }

    @LayoutRes
    @Override
    public final int getItemViewType(final int position) {
        return R.layout.item_cat_fact;
    }

    public static final class DiffUtilCallback<T> extends DiffUtil.Callback {

        private final List<T> oldList;
        private final List<T> newList;

        DiffUtilCallback(final List<T> oldList, final List<T> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            final T oldItem = oldList.get(oldItemPosition);
            final T newItem = newList.get(newItemPosition);

            return oldItem.hashCode() == newItem.hashCode();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            final T oldItem = oldList.get(oldItemPosition);
            final T newItem = newList.get(newItemPosition);
            return Objects.equals(oldItem, newItem);
        }
    }
}
