package tz.app.sample.catfacts.communication.service.catfact;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import tz.app.sample.catfacts.communication.ApiConst;
import tz.app.sample.catfacts.communication.dto.json.catfact.CatFactDto;
import tz.app.sample.catfacts.communication.dto.json.pagedcatfacts.PagedCatFactsDto;

public interface ICatFactApi {

    /**
     * Returns a random cat factTxt.
     * Parameters:
     * max_length:integer - limits the length of the facts returned.
     */
    @Headers("Content-Type: application/json")
    @GET(ApiConst.Path.GET_FACT)
    Call<CatFactDto> getRandomCatFact(@QueryMap final Map<String, String> _query);

    /**
     * Get a list of cat facts
     * <p>
     * Parameters:
     * max_length:integer - limits the length of the facts returned.
     * limit:integer - limit the number of results. Max is 1000 per page.
     */
    @Headers("Content-Type: application/json")
    @GET(ApiConst.Path.GET_FACTS)
    Call<PagedCatFactsDto> getListOfCatFacts(@QueryMap final Map<String, String> _query);

}
