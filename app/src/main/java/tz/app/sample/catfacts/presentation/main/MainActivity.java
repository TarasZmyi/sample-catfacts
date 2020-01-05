package tz.app.sample.catfacts.presentation.main;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.Locale;

import tz.app.sample.catfacts.ObjectGraph;
import tz.app.sample.catfacts.R;
import tz.app.sample.catfacts.domain.runtime.IRuntimeModule;
import tz.app.sample.catfacts.presentation._base.BaseActivity;
import tz.app.sample.catfacts.presentation._base.ModelCache;
import tz.app.sample.catfacts.presentation.main.adapter.CatItemAdapter;
import tz.app.sample.catfacts.presentation.main.adapter.CatItemDataHolder;
import tz.app.sample.catfacts.presentation.main.adapter.CatItemViewHolder;
import tz.app.sample.catfacts.presentation.main.adapter.ICatItemClickListener;
import tz.app.sample.catfacts.presentation.progress.ProgressManager;

public final class MainActivity extends BaseActivity implements IMainView, SwipeRefreshLayout.OnRefreshListener {

    private MainModel mainModel;

    private CatItemAdapter catItemAdapter;

    private TextView tvCurrentLimit;
    private RecyclerView rvCatFacts;
    private SwipeRefreshLayout swipeToRefresh;

    private boolean allowLoadMoreOnScroll;

    private final RecyclerView.OnScrollListener loadMoreScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public final void onScrolled(final @NonNull RecyclerView _rv, final int _dx, final int _dy) {
            if (_dy > 0) {
                final LinearLayoutManager layoutManager = (LinearLayoutManager) _rv.getLayoutManager();
                if (layoutManager == null) {
                    return;
                }

                final int visibleItemCount = layoutManager.getChildCount();
                final int totalItemCount = layoutManager.getItemCount();

                final int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (allowLoadMoreOnScroll) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        allowLoadMoreOnScroll = false;
                        // try load new page;
                        getProgressManager().showLoading("Loading more...");
                        mainModel.loadMoreCatItemData();

                    }
                }
            }
        }
    };

    private final RecyclerView.OnScrollListener parallaxScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public final void onScrolled(final @NonNull RecyclerView _rv, final int _dx, final int _dy) {
            final RecyclerView.LayoutManager layoutManager = _rv.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                final int position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                final CatItemViewHolder firstItemVh = ((CatItemViewHolder) _rv.findViewHolderForLayoutPosition(position));
                if (firstItemVh != null) {
                    final int scrollOffset = _rv.computeVerticalScrollOffset();
                    final IRuntimeModule runtimeModule = getObjectGraph().getRuntimeModule();
                    firstItemVh.updateScrollOffset(scrollOffset, runtimeModule.getItemHeightWithDividerPx());

                    logger.debug(TAG, "parallaxScrollListener, scrollOffset = " + scrollOffset);

                    final CatItemViewHolder nextItemVh = ((CatItemViewHolder) _rv.findViewHolderForLayoutPosition(position + 1));
                    if (nextItemVh != null) {
                        nextItemVh.resetScrollOffset();
                    }
                }
            }
        }
    };

    @Override
    protected final int layoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected final void onCreate(final @Nullable Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);

        final ObjectGraph objectGraph = getObjectGraph();

        if (ModelCache.hasModel(modelKey())) {
            mainModel = ModelCache.retrieveModel(modelKey());
        } else {
            mainModel = new MainModel(objectGraph.getDataModule(), objectGraph.getRuntimeModule());
            ModelCache.storeModel(modelKey(), mainModel);
        }

        rvCatFacts = findViewById(R.id.id_recycler_view_activity_main);
        tvCurrentLimit = findViewById(R.id.id_tv_current_limit_activity_main);
        swipeToRefresh = findViewById(R.id.id_swipe_to_refresh_activity_main);

        setupCatDataList(catItemAdapter = new CatItemAdapter());

        getProgressManager().attach(this);

        if (_savedInstanceState == null) {
            getProgressManager().showLoading();
            mainModel.loadCatItemData();
        } else {
            fillCatData();
        }

    }

    @Override
    protected final void onStart() {
        super.onStart();
        mainModel.setView(this);
        rvCatFacts.addOnScrollListener(loadMoreScrollListener);
        rvCatFacts.addOnScrollListener(parallaxScrollListener);
    }

    @Override
    protected final void onStop() {
        super.onStop();
        mainModel.removeView();
        rvCatFacts.removeOnScrollListener(loadMoreScrollListener);
        rvCatFacts.removeOnScrollListener(parallaxScrollListener);
    }

    @Override
    protected final void onDestroy() {
        super.onDestroy();

        final boolean isActivityFinishing = isFinishing();

        getProgressManager().detach(!isActivityFinishing);

        swipeToRefresh.setOnRefreshListener(null);
        swipeToRefresh = null;

        rvCatFacts = null;

        mainModel = null;

        if (isActivityFinishing) ModelCache.removeModel(modelKey());
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("MainActivity", "onOptionsItemSelected, home");
                rvCatFacts.smoothScrollToPosition(0);
                return true;

            case R.id.menu_item_refresh:
                Log.d("MainActivity", "onOptionsItemSelected, refresh");
                getProgressManager().showLoading("Refreshing...");
                mainModel.loadCatItemData();
                return true;

            case R.id.menu_item_txt_limiter:
                Log.d("MainActivity", "onOptionsItemSelected, limiter");

                final LayoutInflater inflater = getLayoutInflater();
                final TextLimiterHelper textLimiterHelper = new TextLimiterHelper(inflater);
                textLimiterHelper.setupSeekBar(mainModel);

                new AlertDialog.Builder(this)
                        .setView(textLimiterHelper.getCustomView())
                        .setPositiveButton("Refresh", (dialog, which) -> {
                            //
                            getProgressManager().showLoading();
                            mainModel.setTxtFactLengthLimit(textLimiterHelper.getProgress());
                            mainModel.loadCatItemData();
                        })
                        .setOnDismissListener(dialog -> {
                            //
                            textLimiterHelper.release();
                        })
                        .create()
                        .show();
                return true;

            default:
                Log.d("MainActivity", "onOptionsItemSelected, default");
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public final String modelKey() {
        return "MainModel";
    }

    @Override
    public final void onCatDataLoadSuccess() {
        getProgressManager().hideLoading();
        allowLoadMoreOnScroll = true;
        swipeToRefresh.setRefreshing(false);

        fillCatData();
    }

    @Override
    public final void onCatDataLoadFailed() {
        getProgressManager().hideLoading();

        allowLoadMoreOnScroll = false;
        swipeToRefresh.setRefreshing(false);

        rvCatFacts.post(() -> {
            if (catItemAdapter != null) {
                catItemAdapter.notifyDataSetChanged();
            }
        });

        // TODO, show proper errors messages
        Toast.makeText(this, "Load Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void onRefresh() {
        logger.debug(TAG, "onRefresh");
        mainModel.loadCatItemData();
    }

    private void updateTextLimitLabel() {
        final int limit = mainModel.getCurrentLengthCatFact();
        tvCurrentLimit.setText(String.format(Locale.getDefault(), "Cat facts text length is set to %d chars max", limit));
    }

    private void setupCatDataList(final CatItemAdapter catItemAdapter) {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        final DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());

        rvCatFacts.setHasFixedSize(true);
        rvCatFacts.setLayoutManager(layoutManager);
        rvCatFacts.addItemDecoration(itemDecoration);

        rvCatFacts.setAdapter(catItemAdapter);

        swipeToRefresh.setOnRefreshListener(this);
        swipeToRefresh.setColorSchemeResources(R.color.grey);
    }

    private void fillCatData() {
        final List<CatItemDataHolder> catItemDataHolders = mainModel.getCatItemData();
        final ICatItemClickListener listener = mainModel.getCatItemClickListener();

        for (final CatItemDataHolder catItemDataHolder : catItemDataHolders) {
            catItemDataHolder.setCatItemClickListener(listener);
        }

        catItemAdapter.updateData(catItemDataHolders);

        updateTextLimitLabel();
    }

    private ProgressManager getProgressManager() {
        return mainModel.getProgressManager();
    }

    private static final class TextLimiterHelper {

        private int progress = -1;

        private final View rootView;
        private final SeekBar sbLengthLimiter;
        private final TextView tvCurrentLimit;

        private final SeekBar.OnSeekBarChangeListener sbListener = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public final void onProgressChanged(final SeekBar seekBar,
                                                final int _progress,
                                                final boolean _fromUser) {
                setLengthLimit(_progress);
            }

            @Override
            public final void onStartTrackingTouch(final SeekBar seekBar) {

            }

            @Override
            public final void onStopTrackingTouch(final SeekBar seekBar) {

            }
        };

        @UiThread
        @SuppressWarnings("inflateParams")
        TextLimiterHelper(final LayoutInflater inflater) {
            rootView = inflater.inflate(R.layout.view_txt_limiter, null);
            tvCurrentLimit = rootView.findViewById(R.id.id_tv_current_limit_dialog);
            sbLengthLimiter = rootView.findViewById(R.id.id_sb_txt_limiter_dialog);
        }

        final int getProgress() {
            return progress;
        }

        final void release() {
            sbLengthLimiter.setOnSeekBarChangeListener(null);
        }

        final View getCustomView() {
            return rootView;
        }

        private void setupSeekBar(final MainModel _mainModel) {
            final int lengthLimitDefault = _mainModel.getCurrentLengthCatFact();

            sbLengthLimiter.setMax(_mainModel.getMaxLengthCatFact());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sbLengthLimiter.setMin(_mainModel.getMinLengthCatFact());
            }
            sbLengthLimiter.setProgress(lengthLimitDefault);
            sbLengthLimiter.setOnSeekBarChangeListener(sbListener);

            setLengthLimit(lengthLimitDefault);
        }

        private void setLengthLimit(final int _progress) {
            progress = _progress;
            tvCurrentLimit.setText(String.format(Locale.getDefault(), "Text Length limit: %d", _progress));
        }
    }

}
