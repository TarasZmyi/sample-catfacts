package tzpace.app.catfactssample.communication.service.catfact;

import java.util.List;

import tzpace.app.catfactssample.communication.dto.json.catfact.CatFactDto;
import tzpace.app.catfactssample.communication.dto.json.pagedcatfacts.PagedCatFactsDto;
import tzpace.app.catfactssample.communication.dto.xml.catimg.CatImgDto;
import tzpace.app.catfactssample.domain.model.CatFact;
import tzpace.app.catfactssample.domain.model.CatImg;
import tzpace.app.catfactssample.domain.model.PagesHelper;

public interface IEntityTranslator {

    CatFact createCatFact(CatFactDto _dto);

    List<CatFact> createCatFactsData(List<CatFactDto> _dto);

    List<CatImg> createCatImagesData(CatImgDto _dto);

    PagesHelper createPagesData(PagedCatFactsDto _dto);

}
