package tzpace.app.catfactssample.communication.service.catimg;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import tzpace.app.catfactssample.communication.ApiConst;
import tzpace.app.catfactssample.communication.dto.json.catimg.CatImgDto;

public interface ICatImgApi {

    /**
     * Get a list of cat images
     * <p>
     * Parameters:
     * results_per_page:integer | The number of Cats to respond with.
     * type:string | A comma separated string of file types to return. e.g. jpg,png.
     * format:string | The output format, as XML, an HTML img tag, or the src to use in an img tag.
     */
    @GET(ApiConst.Path.GET_IMAGES)
    Call<List<CatImgDto>> getListOfCatImages(@QueryMap final Map<String, String> _query);

}
