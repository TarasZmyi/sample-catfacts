package tz.app.sample.catfacts.communication.service.catfact;

import java.util.List;

import tz.app.sample.catfacts.communication.dto.json.catfact.CatFactDto;
import tz.app.sample.catfacts.communication.dto.json.catimg.CatImgDto;
import tz.app.sample.catfacts.communication.dto.json.pagedcatfacts.PagedCatFactsDto;
import tz.app.sample.catfacts.domain.model.CatFact;
import tz.app.sample.catfacts.domain.model.CatImg;
import tz.app.sample.catfacts.domain.model.PagesHelper;

public interface IEntityTranslator {

    CatFact createCatFact(CatFactDto _dto);

    List<CatFact> createCatFactsData(List<CatFactDto> _dto);

    List<CatImg> createCatImagesData(List<CatImgDto> _dto);

    PagesHelper createPagesData(PagedCatFactsDto _dto);

}
