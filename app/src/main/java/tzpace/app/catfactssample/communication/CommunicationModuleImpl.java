package tzpace.app.catfactssample.communication;

import java.net.MalformedURLException;
import java.net.URL;

import tzpace.app.catfactssample.communication.service.ApiHelper;
import tzpace.app.catfactssample.communication.service.catfact.CatFactServiceImpl;
import tzpace.app.catfactssample.communication.service.catimg.CatImgServiceImpl;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

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
