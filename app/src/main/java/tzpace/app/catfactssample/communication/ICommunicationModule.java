package tzpace.app.catfactssample.communication;

import tzpace.app.catfactssample.communication.dto.BaseDto;
import tzpace.app.catfactssample.communication.dto.json.catfact.CatFactDto;
import tzpace.app.catfactssample.communication.dto.json.pagedcatfacts.PagedCatFactsDto;
import tzpace.app.catfactssample.communication.dto.xml.catimg.CatImgDto;

public interface ICommunicationModule {

    CatFactService getCatFactService();

    CatImgService getCatImgService();

    interface CatFactService {

        void getRandomCatFact(ServiceCallback<CatFactDto> _callback);

        void setTxtFactMaxLength(int _txtFactMaxLength);

        void setNextPage(int _nextPage);

        void getListOfCatFacts(ServiceCallback<PagedCatFactsDto> _callback);

    }

    interface CatImgService {

        void getListOfCatImages(ServiceCallback<CatImgDto> _callback);

    }

    interface ServiceCallback<T extends BaseDto> {

        void onSuccess(T _response);

        void onError(String _error);

    }

}
