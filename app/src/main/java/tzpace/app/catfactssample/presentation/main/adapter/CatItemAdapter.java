package tzpace.app.catfactssample.presentation.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tzpace.app.catfactssample.R;
import tzpace.app.catfactssample.domain.runtime.IRuntimeModule;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

public final class CatItemAdapter extends RecyclerView.Adapter<CatItemViewHolder> {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final List<CatItemDataHolder> catItemDataHolders = new ArrayList<>();

    private final IRuntimeModule runtimeModule;

    public CatItemAdapter(final IRuntimeModule _runtimeModule) {
        runtimeModule = _runtimeModule;
    }

    public void updateData(final List<CatItemDataHolder> catItemData) {
        logger.debug(TAG, "updateData | size = " + catItemData.size());
        clear();
        catItemDataHolders.addAll(catItemData);
        notifyDataSetChanged();
    }

    public void clearData() {
        logger.debug(TAG, "clearData");
        clear();
        notifyDataSetChanged();
    }

    private void clear() {
        catItemDataHolders.clear();
    }

    @Override
    public final CatItemViewHolder onCreateViewHolder(final ViewGroup parent, final @LayoutRes int viewType) {
        logger.debug(TAG, "onCreateViewHolder");
        final Context context = parent.getContext();
        final View root = LayoutInflater.from(context).inflate(viewType, parent, false);
        return new CatItemViewHolder(root, runtimeModule);
    }

    @Override
    public final void onBindViewHolder(CatItemViewHolder holder, int position) {
        logger.debug(TAG, "onBindViewHolder | pos = " + position);
        final CatItemDataHolder catItemDataHolder = catItemDataHolders.get(position);
        holder.bindData(catItemDataHolder);
    }

    @Override
    public void onViewRecycled(@NonNull CatItemViewHolder holder) {
        super.onViewRecycled(holder);
        logger.debug(TAG, "onViewRecycled");
        holder.unbindData();

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
}
