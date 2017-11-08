package tzpace.app.catfactssample.domain.catdata;

import java.util.List;

import tzpace.app.catfactssample.domain.model.CatFact;
import tzpace.app.catfactssample.domain.model.CatImg;

public interface ICatDataModule {

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
