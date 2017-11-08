package tzpace.app.catfactssample.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import tzpace.app.catfactssample.ObjectGraph;
import tzpace.app.catfactssample.R;
import tzpace.app.catfactssample.domain.model.CatFact;
import tzpace.app.catfactssample.presentation.BaseActivity;
import tzpace.app.catfactssample.presentation.ModelCache;
import tzpace.app.catfactssample.presentation.main.adapter.CatItemAdapter;
import tzpace.app.catfactssample.presentation.main.adapter.CatItemDataHolder;
import tzpace.app.catfactssample.presentation.main.adapter.ICatItemClickListener;
import tzpace.app.catfactssample.utils.progress.ProgressManager;

public final class MainActivity extends BaseActivity implements IMainView, ICatItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private MainModel mainModel;

    private SeekBar sbLengthLimiter;
    private TextView tvCurrentLimit;

    private RecyclerView rvCatFacts;
    private SwipeRefreshLayout swipeToRefresh;

    private CatItemAdapter catItemAdapter;

    private boolean allowLoadMoreOnScroll;

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
            mainModel = new MainModel(objectGraph.getCatDataModule());
            ModelCache.storeModel(modelKey(), mainModel);
        }

        rvCatFacts = findViewById(R.id.id_recycler_view_activity_main);
        swipeToRefresh = findViewById(R.id.id_swipe_to_refresh_activity_main);

        tvCurrentLimit = findViewById(R.id.id_tv_current_limit_activity_main);
        sbLengthLimiter = findViewById(R.id.id_seek_bar_activity_main);

        setupSeekBar();

        setupRecyclerView(catItemAdapter = new CatItemAdapter(objectGraph.getRuntimeModule()));

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
        rvCatFacts.addOnScrollListener(scrollListener);
    }

    @Override
    protected final void onStop() {
        super.onStop();
        mainModel.removeView();
        rvCatFacts.removeOnScrollListener(scrollListener);
    }

    @Override
    protected final void onDestroy() {
        super.onDestroy();

        final boolean isActivityFinishing = isFinishing();

        getProgressManager().detach(!isActivityFinishing);

        swipeToRefresh.setOnRefreshListener(null);
        swipeToRefresh = null;
        sbLengthLimiter.setOnSeekBarChangeListener(null);
        sbLengthLimiter = null;
        rvCatFacts = null;

        mainModel = null;
        if (isActivityFinishing) ModelCache.removeModel(modelKey());
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
        Toast.makeText(this, "Load Failed", Toast.LENGTH_SHORT).show(); // TODO | show proper errors messages
        allowLoadMoreOnScroll = false;
        swipeToRefresh.setRefreshing(false);
        catItemAdapter.notifyDataSetChanged();
    }

    @Override
    public final void onCatItemClickedShare(final CatFact _catFact) {
        logger.debug(TAG, "onCatItemClickedShare | CatFact = " + _catFact);

        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, _catFact.factTxt);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_cat_fact)));

    }

    @Override
    public final void onRefresh() {
        logger.debug(TAG, "onRefresh");
        mainModel.setTxtFactLengthLimit(sbLengthLimiter.getProgress());
        mainModel.loadCatItemData();
    }

    private void setupSeekBar() {
        final int lengthLimitDefault = mainModel.getDefaultLengthCatFact();
        sbLengthLimiter.setMax(mainModel.getMaxLengthCatFact());
        sbLengthLimiter.setProgress(lengthLimitDefault);
        sbLengthLimiter.setOnSeekBarChangeListener(sbChangeListener);
        setLengthLimitText(lengthLimitDefault);
    }

    private void setupRecyclerView(final CatItemAdapter catItemAdapter) {
        rvCatFacts.setLayoutManager(new LinearLayoutManager(this));
        rvCatFacts.setAdapter(catItemAdapter);
        swipeToRefresh.setOnRefreshListener(this);
        swipeToRefresh.setColorSchemeResources(R.color.grey);
    }

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public final void onScrolled(final RecyclerView _recyclerView, final int _dx, final int _dy) {
            if (_dy > 0) {
                final LinearLayoutManager layoutManager = (LinearLayoutManager) _recyclerView.getLayoutManager();
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

    private final SeekBar.OnSeekBarChangeListener sbChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public final void onProgressChanged(final SeekBar seekBar, final int _progress, final boolean _fromUser) {
            mainModel.setTxtFactLengthLimit(_progress);
            setLengthLimitText(_progress);
        }

        @Override
        public final void onStartTrackingTouch(final SeekBar seekBar) {
            // catItemAdapter.clearData();
        }

        @Override
        public final void onStopTrackingTouch(final SeekBar seekBar) {
            getProgressManager().showLoading();
            mainModel.loadCatItemData();
        }
    };

    private void setLengthLimitText(final int _progress) {
        tvCurrentLimit.setText(String.format(Locale.getDefault(), "Text Length limit: %d", _progress));
    }

    private void fillCatData() {
        final List<CatItemDataHolder> catItemDataHolders = mainModel.getCatItemData();

        for (final CatItemDataHolder catItemDataHolder : catItemDataHolders) {
            catItemDataHolder.setCatItemClickListener(this);
        }

        catItemAdapter.updateData(catItemDataHolders);
    }

    private ProgressManager getProgressManager() {
        return mainModel.getProgressManager();
    }

}
