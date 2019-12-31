package tz.app.sample.catfacts.domain.catdata;

import java.util.List;

import tz.app.sample.catfacts.domain.model.CatFact;
import tz.app.sample.catfacts.domain.model.CatImg;

public interface IDataModule {

    void loadRandomCatFact(Callback _callback);

    CatFact getRandomCatFact();

    void loadCatFactsData(int _txtFactMaxLength, Callback _callback);

    void loadCatImagesData(Callback _callback);

    List<CatFact> getCatFactsData();

    List<CatImg> getCatImagesData();

    boolean hasNextPage();

    void setLoadNextPage(boolean _doLoadNext);

    void clearPage();

    interface Callback {

        void success();

        void fail();

    }
}
