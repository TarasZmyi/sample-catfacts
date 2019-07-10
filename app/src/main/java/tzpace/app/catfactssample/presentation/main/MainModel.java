package tzpace.app.catfactssample.presentation.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import tzpace.app.catfactssample.domain.catdata.ICatDataModule;
import tzpace.app.catfactssample.domain.model.CatFact;
import tzpace.app.catfactssample.domain.model.CatImg;
import tzpace.app.catfactssample.presentation.BaseModel;
import tzpace.app.catfactssample.presentation.main.adapter.CatItemDataHolder;
import tzpace.app.catfactssample.utils.commander.CommanderImpl;
import tzpace.app.catfactssample.utils.commander.ICommand;
import tzpace.app.catfactssample.utils.commander.ICommander;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;
import tzpace.app.catfactssample.utils.progress.ProgressManager;

/**
 * kind of Fat Model for main screen.
 */
final class MainModel extends BaseModel<IMainView> {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final ICatDataModule catDataModule;

    private final List<CatImg> catImgData = new ArrayList<>();
    private final List<CatFact> catFactData = new ArrayList<>();

    private final List<CatItemDataHolder> catItemData = new ArrayList<>();

    private final ICommander commander = new CommanderImpl();

    @Nullable
    private IMainView mainView;

    private int txtFactLengthLimit = getDefaultLengthCatFact();

    private ProgressManager progressManager;

    MainModel(final ICatDataModule _catDataModule) {
        catDataModule = _catDataModule;
        progressManager = new ProgressManager();
    }

    @Override
    public void setView(@NonNull IMainView _mainView) {
        logger.debug(TAG, "setMainView");
        mainView = _mainView;

        if (commander.hasCommands()) {
            logger.debug(TAG, "Commander hasCommands | executing Commands");
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
            logger.debug(TAG, "loadMoreCatItemData | END page reached.");
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

    final int getDefaultLengthCatFact() {
        logger.debug(TAG, "getDefaultLengthCatFact = " + 100);
        return 100;
    }

    final void setTxtFactLengthLimit(final int _txtFactLengthLimit) {
        logger.debug(TAG, "setTxtFactLengthLimit | limit = " + _txtFactLengthLimit);
        txtFactLengthLimit = _txtFactLengthLimit;
    }

    final List<CatItemDataHolder> getCatItemData() {

        final int catFactDataSize = catFactData.size();
        final int catImgDataSize = catImgData.size();

        catItemData.clear();

        for (int i = 0; i < catFactDataSize; i++) {
            final CatItemDataHolder catItemDataHolder = new CatItemDataHolder();
            catItemDataHolder.catFact = catFactData.get(i);
            if (catImgDataSize >= catFactDataSize) {
                catItemDataHolder.catImg = catImgData.get(i);
            }
            catItemData.add(catItemDataHolder);
        }

        return catItemData;
    }

    final ProgressManager getProgressManager() {
        return progressManager;
    }

    private final ICatDataModule.Callback loadCatFactsDataCallback = new ICatDataModule.Callback() {

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

    private final ICatDataModule.Callback loadCatImagesDataCallback = new ICatDataModule.Callback() {

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

    private final ICatDataModule.Callback loadMoreCatFactsDataCallback = new ICatDataModule.Callback() {

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

    private final ICatDataModule.Callback loadMoreCatImagesDataCallback = new ICatDataModule.Callback() {

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

    private static abstract class CompositeCallback {

        private int counter;
        private final int count;

        CompositeCallback(final int _count) {
            count = _count;
        }

        void process() {
            counter = counter + 1;

            if (counter == count) {
                onSuccess();
            }
        }

        void clear() {
            counter = 0;
        }

        abstract void onSuccess();

        abstract void onError();
    }

    private static class CatDataLoadCommand implements ICommand {

        private final boolean isSuccess;
        private MainModel mainModel;

        CatDataLoadCommand(final boolean _isSuccess, final MainModel _mainModel) {
            isSuccess = _isSuccess;
            mainModel = _mainModel;
        }

        @Override
        public void execute() {

            if (mainModel.mainView == null) {
                mainModel.logger.debug(mainModel.TAG, "CatDataLoadCommand | skip");
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
