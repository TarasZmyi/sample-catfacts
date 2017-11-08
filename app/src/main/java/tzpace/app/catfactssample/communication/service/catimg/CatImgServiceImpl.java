package tzpace.app.catfactssample.communication.service.catimg;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import tzpace.app.catfactssample.communication.ApiConst;
import tzpace.app.catfactssample.communication.ICommunicationModule;
import tzpace.app.catfactssample.communication.dto.xml.catimg.CatImgDto;
import tzpace.app.catfactssample.communication.service.ApiCallback;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

public class CatImgServiceImpl implements ICommunicationModule.CatImgService {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final ICatImgApi catImgApi;

    public CatImgServiceImpl(final ICatImgApi _catImgApi) {
        catImgApi = _catImgApi;
    }

    @Override
    public void getListOfCatImages(ICommunicationModule.ServiceCallback<CatImgDto> _callback) {
        logger.debug(TAG, "getListOfCatImages");
        final Map<String, String> query = new HashMap<>();
        query.put("format", "xml");
        query.put("results_per_page", String.valueOf(ApiConst.PAGE_SIZE));
        query.put("type", "jpg,png");
        //query.put("size", "small"); // not good
        final Call<CatImgDto> call = catImgApi.getListOfCatImages(query);
        final ApiCallback<CatImgDto> apiCallback = new ApiCallback<>(_callback);
        call.enqueue(apiCallback);
    }

}
