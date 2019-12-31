package tz.app.sample.catfacts.communication.service.catimg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import tz.app.sample.catfacts.communication.ApiConst;
import tz.app.sample.catfacts.communication.ICommunicationModule;
import tz.app.sample.catfacts.communication.dto.json.catimg.CatImgDto;
import tz.app.sample.catfacts.communication.service.ApiCallback;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

public class CatImgServiceImpl implements ICommunicationModule.CatImgService {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    private final ICatImgApi catImgApi;

    public CatImgServiceImpl(final ICatImgApi _catImgApi) {
        catImgApi = _catImgApi;
    }

    @Override
    public void getListOfCatImages(ICommunicationModule.ServiceCallback<List<CatImgDto>> _callback) {
        logger.debug(TAG, "getListOfCatImages");
        final Map<String, String> query = new HashMap<>();
        query.put("limit", String.valueOf(ApiConst.PAGE_SIZE)); // (min 1, max 100)
        query.put("mime_types", "jpg,png");
        query.put("size", "med"); // (allow: full, med, small, thumb)
        final Call<List<CatImgDto>> call = catImgApi.getListOfCatImages(query);
        final ApiCallback<List<CatImgDto>> apiCallback = new ApiCallback<>(_callback);
        call.enqueue(apiCallback);
    }

}
