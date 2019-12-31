package tz.app.sample.catfacts.communication;

import java.util.List;

import tz.app.sample.catfacts.communication.dto.json.catfact.CatFactDto;
import tz.app.sample.catfacts.communication.dto.json.catimg.CatImgDto;
import tz.app.sample.catfacts.communication.dto.json.pagedcatfacts.PagedCatFactsDto;

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

        void getListOfCatImages(ServiceCallback<List<CatImgDto>> _callback);

    }

    interface ServiceCallback<T/* extends BaseDto*/> {

        void onSuccess(T _response);

        void onError(String _error);

    }

}
