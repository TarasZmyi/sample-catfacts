package tz.app.sample.catfacts.communication;

import java.net.MalformedURLException;
import java.net.URL;

import tz.app.sample.catfacts.communication.service.ApiHelper;
import tz.app.sample.catfacts.communication.service.catfact.CatFactServiceImpl;
import tz.app.sample.catfacts.communication.service.catimg.CatImgServiceImpl;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

public final class CommunicationModuleImpl implements ICommunicationModule {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final CatFactService catFactService;
    private final CatImgService catImgService;

    public CommunicationModuleImpl(final ApiHelper apiHelper) {
        catFactService = new CatFactServiceImpl(apiHelper.createCatFactApi(getCatFactServerAddress()));
        catImgService = new CatImgServiceImpl(apiHelper.createCatImgApi(getCatImgServerAddress()));
    }

    @Override
    public CatFactService getCatFactService() {
        logger.debug(TAG, "getCatFactService");
        return catFactService;
    }

    @Override
    public CatImgService getCatImgService() {
        logger.debug(TAG, "getCatImgService");
        return catImgService;
    }

    private URL getCatFactServerAddress() {
        try {
            return new URL(ApiConst.BASE_URL_CAT_FACT);
        } catch (final MalformedURLException _e) {
            logger.error(TAG, "getCatFactServerAddress", _e);
            throw new RuntimeException("Malformed address");
        }
    }

    private URL getCatImgServerAddress() {
        try {
            return new URL(ApiConst.BASE_URL_CAT_IMG);
        } catch (final MalformedURLException _e) {
            logger.error(TAG, "getCatImgServerAddress", _e);
            throw new RuntimeException("Malformed address");
        }
    }

}
