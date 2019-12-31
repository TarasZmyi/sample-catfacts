package tz.app.sample.catfacts.domain.catdata;

import androidx.annotation.Nullable;

import java.util.List;

import tz.app.sample.catfacts.communication.ICommunicationModule;
import tz.app.sample.catfacts.communication.dto.json.catfact.CatFactDto;
import tz.app.sample.catfacts.communication.dto.json.catimg.CatImgDto;
import tz.app.sample.catfacts.communication.dto.json.pagedcatfacts.PagedCatFactsDto;
import tz.app.sample.catfacts.domain.model.CatFact;
import tz.app.sample.catfacts.domain.model.CatImg;
import tz.app.sample.catfacts.domain.model.PagesHelper;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

public final class DataModuleImpl implements IDataModule {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final ICommunicationModule.CatFactService catFactService;
    private final ICommunicationModule.CatImgService catImgService;

    private final EntityTranslator entityTranslator;
    private boolean isLoadNextPage;
    @Nullable
    private PagesHelper pagesHelper;
    private List<CatFact> catFactsData;
    private Callback catFactsDataCallback;
    private final ICommunicationModule.ServiceCallback<PagedCatFactsDto> loadCatFactsDataServiceCallback = new ICommunicationModule.ServiceCallback<PagedCatFactsDto>() {

        @Override
        public final void onSuccess(final PagedCatFactsDto _response) {
            logger.debug(TAG, "loadCatFactsDataServiceCallback, onSuccess");

            pagesHelper = entityTranslator.createPagesData(_response);
            catFactsData = entityTranslator.createCatFactsData(_response.getData());

            if (catFactsDataCallback != null) catFactsDataCallback.success();
        }

        @Override
        public final void onError(final String _error) {
            logger.debug(TAG, "loadCatFactsDataServiceCallback, onError");

            catFactsData = null;

            if (catFactsDataCallback != null) catFactsDataCallback.fail();
        }
    };
    private CatFact randomCatFact;
    private Callback randomCatFactCallback;
    private final ICommunicationModule.ServiceCallback<CatFactDto> loadRandomCatFactServiceCallback = new ICommunicationModule.ServiceCallback<CatFactDto>() {

        @Override
        public final void onSuccess(final CatFactDto _response) {
            logger.debug(TAG, "loadRandomCatFactServiceCallback, onSuccess");

            randomCatFact = entityTranslator.createCatFact(_response);

            if (randomCatFactCallback != null) randomCatFactCallback.success();
        }

        @Override
        public final void onError(final String _error) {
            logger.debug(TAG, "loadRandomCatFactServiceCallback, onError");

            randomCatFact = null;

            if (randomCatFactCallback != null) randomCatFactCallback.fail();
        }
    };
    private List<CatImg> catImagesData;
    private Callback catImagesDataCallback;
    private final ICommunicationModule.ServiceCallback<List<CatImgDto>> loadCatImagesDataServiceCallback = new ICommunicationModule.ServiceCallback<List<CatImgDto>>() {
        @Override
        public void onSuccess(List<CatImgDto> _response) {
            logger.debug(TAG, "loadCatImagesDataServiceCallback, onSuccess");

            catImagesData = entityTranslator.createCatImagesData(_response);

            if (catImagesDataCallback != null) catImagesDataCallback.success();
        }

        @Override
        public void onError(String _error) {
            logger.debug(TAG, "loadCatImagesDataServiceCallback, onError");

            catImagesData = null;

            if (catImagesDataCallback != null) catImagesDataCallback.fail();
        }
    };

    public DataModuleImpl(final ICommunicationModule.CatFactService _catFactService, final ICommunicationModule.CatImgService _catImgService) {
        catFactService = _catFactService;
        catImgService = _catImgService;

        entityTranslator = new EntityTranslator();
    }

    @Override
    public final void loadRandomCatFact(final Callback _callback) {
        logger.debug(TAG, "loadRandomCatFact");
        randomCatFactCallback = _callback;
        catFactService.getRandomCatFact(loadRandomCatFactServiceCallback);
    }

    @Override
    public CatFact getRandomCatFact() {
        return randomCatFact;
    }

    @Override
    public final void loadCatFactsData(int _txtFactMaxLength, final Callback _callback) {
        logger.debug(TAG, "loadCatFactsData");
        catFactsDataCallback = _callback;

        if (pagesHelper != null && isLoadNextPage) {
            catFactService.setNextPage(pagesHelper.getNextPageInt());
        } else {
            catFactService.setNextPage(-1);
        }

        catFactService.setTxtFactMaxLength(_txtFactMaxLength);
        catFactService.getListOfCatFacts(loadCatFactsDataServiceCallback);
    }

    @Override
    public void loadCatImagesData(Callback _callback) {
        logger.debug(TAG, "loadCatImagesData");
        catImagesDataCallback = _callback;
        catImgService.getListOfCatImages(loadCatImagesDataServiceCallback);
    }

    @Override
    public List<CatFact> getCatFactsData() {
        return catFactsData;
    }

    @Override
    public List<CatImg> getCatImagesData() {
        return catImagesData;
    }

    @Override
    public boolean hasNextPage() {
        return pagesHelper != null && !pagesHelper.isLastPage();
    }

    @Override
    public void setLoadNextPage(boolean _loadNext) {
        logger.debug(TAG, "setLoadNextPage, loadNext = " + _loadNext);
        isLoadNextPage = _loadNext;
    }

    @Override
    public void clearPage() {
        logger.debug(TAG, "clearPage");
        pagesHelper = null;
        isLoadNextPage = false;
    }
}
