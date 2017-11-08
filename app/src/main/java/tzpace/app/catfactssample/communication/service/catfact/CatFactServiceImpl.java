package tzpace.app.catfactssample.communication.service.catfact;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import tzpace.app.catfactssample.communication.ApiConst;
import tzpace.app.catfactssample.communication.ICommunicationModule;
import tzpace.app.catfactssample.communication.dto.json.catfact.CatFactDto;
import tzpace.app.catfactssample.communication.dto.json.pagedcatfacts.PagedCatFactsDto;
import tzpace.app.catfactssample.communication.service.ApiCallback;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

public final class CatFactServiceImpl implements ICommunicationModule.CatFactService {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final ICatFactApi catFactApi;

    private int txtFactMaxLength = -1; // not set, default;

    private int nextPage = -1; // not set, default;

    public CatFactServiceImpl(final ICatFactApi _catFactApi) {
        catFactApi = _catFactApi;
    }

    @Override
    public final void getRandomCatFact(final ICommunicationModule.ServiceCallback<CatFactDto> _callback) {
        logger.debug(TAG, "getRandomCatFact");
        final Map<String, String> query = new HashMap<>();
        final Call<CatFactDto> call = catFactApi.getRandomCatFact(query);
        final ApiCallback<CatFactDto> apiCallback = new ApiCallback<>(_callback);
        call.enqueue(apiCallback);
    }

    @Override
    public final void setTxtFactMaxLength(final int _txtFactMaxLength) {
        logger.debug(TAG, "setTxtFactMaxLength | Length = " + _txtFactMaxLength);
        txtFactMaxLength = _txtFactMaxLength;
    }

    @Override
    public final void setNextPage(final int _nextPage) {
        logger.debug(TAG, "setNextPage | nextPage = " + _nextPage);
        nextPage = _nextPage;
    }

    @Override
    public final void getListOfCatFacts(final ICommunicationModule.ServiceCallback<PagedCatFactsDto> _callback) {
        logger.debug(TAG, "getListOfCatFacts");
        final Map<String, String> query = new HashMap<>();
        query.put(ApiConst.QueryParam.INT_LIMIT, String.valueOf(ApiConst.PAGE_SIZE));

        if (nextPage > 0) {
            logger.debug(TAG, "requesting page = " + nextPage);
            query.put(ApiConst.QueryParam.INT_PAGE, String.valueOf(nextPage));
        }

        if (txtFactMaxLength > 0) {
            query.put(ApiConst.QueryParam.INT_MAX_LENGTH, String.valueOf(txtFactMaxLength));
        }

        final Call<PagedCatFactsDto> call = catFactApi.getListOfCatFacts(query);
        final ApiCallback<PagedCatFactsDto> apiCallback = new ApiCallback<>(_callback);
        call.enqueue(apiCallback);
    }
}
