package tz.app.sample.catfacts.presentation.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import tz.app.sample.catfacts.domain.catdata.IDataModule;
import tz.app.sample.catfacts.domain.model.CatFact;
import tz.app.sample.catfacts.domain.model.CatImg;
import tz.app.sample.catfacts.domain.runtime.IRuntimeModule;
import tz.app.sample.catfacts.presentation._base.BaseModel;
import tz.app.sample.catfacts.presentation.main.adapter.CatItemDataHolder;
import tz.app.sample.catfacts.presentation.main.adapter.ICatItemClickListener;
import tz.app.sample.catfacts.presentation.progress.ProgressManager;
import tz.app.sample.catfacts.utils.cmd.Commander;
import tz.app.sample.catfacts.utils.cmd.ICommand;
import tz.app.sample.catfacts.utils.cmd.ICommander;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

/**
 * Kind of Fat Model for main screen.
 */
final class MainModel extends BaseModel<IMainView> {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    @Nullable
    private IMainView mainView;

    private int txtFactLengthLimit = 100;

    private final ICommander commander;
    private final IDataModule catDataModule;
    private final IRuntimeModule runtimeModule;
    private final ProgressManager progressManager;

    private final List<CatImg> catImgData = new ArrayList<>();
    private final List<CatFact> catFactData = new ArrayList<>();
    private final List<CatItemDataHolder> catItemData = new ArrayList<>();

    private final IDataModule.Callback loadCatFactsDataCallback = new IDataModule.Callback() {

        @Override
        public void success() {
            catFactData.clear();
            catFactData.addAll(catDataModule.getCatFactsData());
            doubleCompositeCallback.process();
        }

        @Override
        public void fail() {
            catFactData.clear();
            doubleCompositeCallback.onError();
        }

    };
    private final IDataModule.Callback loadCatImagesDataCallback = new IDataModule.Callback() {

        @Override
        public void success() {
            catImgData.clear();
            catImgData.addAll(catDataModule.getCatImagesData());
            doubleCompositeCallback.process();
        }

        @Override
        public void fail() {
            catFactData.clear();
            doubleCompositeCallback.onError();
        }

    };
    private final IDataModule.Callback loadMoreCatFactsDataCallback = new IDataModule.Callback() {

        @Override
        public void success() {
            catFactData.addAll(catDataModule.getCatFactsData());
            doubleCompositeCallback.process();
        }

        @Override
        public void fail() {
            doubleCompositeCallback.onError();
        }

    };
    private final IDataModule.Callback loadMoreCatImagesDataCallback = new IDataModule.Callback() {

        @Override
        public void success() {
            catImgData.addAll(catDataModule.getCatImagesData());
            doubleCompositeCallback.process();
        }

        @Override
        public void fail() {
            doubleCompositeCallback.onError();
        }

    };

    private final ICatItemClickListener catItemClickListener = _catFact -> {
        logger.debug(TAG, "onCatItemClickedShare, CatFact = " + _catFact);
        shareCatFact(_catFact.factTxt);
    };

    private final CompositeCallback doubleCompositeCallback = new CompositeCallback(2) {

        @Override
        void onSuccess() {
            if (mainView != null) {
                mainView.onCatDataLoadSuccess();
            } else {
                final ICommand cmd = new CatDataLoadCommand(true, MainModel.this);
                commander.storeCommand(cmd);
            }
        }

        @Override
        void onError() {
            if (mainView != null) {
                mainView.onCatDataLoadFailed();
            } else {
                final ICommand cmd = new CatDataLoadCommand(false, MainModel.this);
                commander.storeCommand(cmd);
            }
        }
    };

    MainModel(final IDataModule _catDataModule, final IRuntimeModule _runtimeModule) {
        catDataModule = _catDataModule;
        runtimeModule = _runtimeModule;

        commander = new Commander();
        progressManager = new ProgressManager();
    }

    @Override
    public void setView(@NonNull IMainView _mainView) {
        logger.debug(TAG, "setMainView");
        mainView = _mainView;

        if (commander.hasCommands()) {
            logger.debug(TAG, "Commander hasCommands, executing Commands");
            commander.executeCommands();
        }
    }

    @Override
    public void removeView() {
        logger.debug(TAG, "removeMainView");
        mainView = null;
    }

    final void loadCatItemData() {
        logger.debug(TAG, "loadCatItemData");

        catDataModule.clearPage();
        doubleCompositeCallback.clear();

        catDataModule.loadCatFactsData(txtFactLengthLimit, loadCatFactsDataCallback);
        catDataModule.loadCatImagesData(loadCatImagesDataCallback);

    }

    final void loadMoreCatItemData() {
        logger.debug(TAG, "loadMoreCatItemData");

        if (catDataModule.hasNextPage()) {
            catDataModule.setLoadNextPage(true);
        } else {
            catDataModule.setLoadNextPage(false);
            doubleCompositeCallback.onError();
            logger.debug(TAG, "loadMoreCatItemData, END page reached.");
            return;
        }

        doubleCompositeCallback.clear();

        catDataModule.loadCatFactsData(txtFactLengthLimit, loadMoreCatFactsDataCallback);
        catDataModule.loadCatImagesData(loadMoreCatImagesDataCallback);

    }

    final int getMaxLengthCatFact() {
        logger.debug(TAG, "getMaxLengthCatFact = " + 500);
        return 500;
    }

    final int getMinLengthCatFact() {
        logger.debug(TAG, "getMinLengthCatFact = " + 1);
        return 1;
    }

    final int getCurrentLengthCatFact() {
        logger.debug(TAG, "getCurrentLengthCatFact = " + txtFactLengthLimit);
        return txtFactLengthLimit;
    }

    final ICatItemClickListener getCatItemClickListener() {
        logger.debug(TAG, "getCatItemClickListener");
        return catItemClickListener;
    }

    final void setTxtFactLengthLimit(final int _txtFactLengthLimit) {
        logger.debug(TAG, "setTxtFactLengthLimit, limit = " + _txtFactLengthLimit);
        txtFactLengthLimit = _txtFactLengthLimit;
    }

    final List<CatItemDataHolder> getCatItemData() {

        final int catFactDataSize = catFactData.size();
        final int catImgDataSize = catImgData.size();

        catItemData.clear();

        for (int i = 0; i < catFactDataSize; i++) {

            final CatImg img;
            if (catImgDataSize >= catFactDataSize) {
                img = catImgData.get(i);
            } else {
                img = null;
            }

            final CatItemDataHolder catItemDataHolder = new CatItemDataHolder(img, catFactData.get(i));

            catItemData.add(catItemDataHolder);
        }

        return catItemData;
    }

    final ProgressManager getProgressManager() {
        return progressManager;
    }

    private void shareCatFact(final String txt) {
        runtimeModule.displayShareCatFact(txt);
    }

    private static abstract class CompositeCallback {

        private final int count;
        private int counter;

        CompositeCallback(final int _count) {
            count = _count;
        }

        final void process() {
            counter = counter + 1;

            if (counter == count) {
                onSuccess();
            }
        }

        final void clear() {
            counter = 0;
        }

        abstract void onSuccess();

        abstract void onError();
    }

    private static final class CatDataLoadCommand implements ICommand {

        private final boolean isSuccess;
        private MainModel mainModel;

        CatDataLoadCommand(final boolean _isSuccess, final MainModel _mainModel) {
            isSuccess = _isSuccess;
            mainModel = _mainModel;
        }

        @Override
        public void execute() {

            if (mainModel.mainView == null) {
                mainModel.logger.debug(mainModel.TAG, "CatDataLoadCommand, skip");
                mainModel = null;
                return;
            }

            if (isSuccess) {
                mainModel.mainView.onCatDataLoadSuccess();
            } else {
                mainModel.mainView.onCatDataLoadFailed();
            }

            mainModel = null;
        }

    }
}
