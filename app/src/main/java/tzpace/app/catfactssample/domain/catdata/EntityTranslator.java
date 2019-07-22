package tzpace.app.catfactssample.domain.catdata;

import java.util.ArrayList;
import java.util.List;

import tzpace.app.catfactssample.communication.dto.json.catfact.CatFactDto;
import tzpace.app.catfactssample.communication.dto.json.pagedcatfacts.PagedCatFactsDto;
import tzpace.app.catfactssample.communication.dto.json.catimg.CatImgDto;
import tzpace.app.catfactssample.communication.service.catfact.IEntityTranslator;
import tzpace.app.catfactssample.domain.model.CatFact;
import tzpace.app.catfactssample.domain.model.CatImg;
import tzpace.app.catfactssample.domain.model.PagesHelper;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

final class EntityTranslator implements IEntityTranslator {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    EntityTranslator() {
        // TODO: inject validation module;
    }

    @Override
    public final CatFact createCatFact(final CatFactDto _dto) {
        logger.debug(TAG, "createCatFact | from = " + _dto);

        // TODO:  validate dto;

        return new CatFact(_dto.getFact());
    }

    @Override
    public List<CatFact> createCatFactsData(List<CatFactDto> _dto) {
        final List<CatFact> data = new ArrayList<>();
        for (final CatFactDto catFactDto : _dto) {
            data.add(createCatFact(catFactDto));
        }
        return data;
    }

    @Override
    public List<CatImg> createCatImagesData(List<CatImgDto> _dto) {
        final List<CatImg> data = new ArrayList<>();
        for (final CatImgDto catImgDto : _dto) {
            data.add(new CatImg(catImgDto.getUrl()));
        }
        return data;
    }

    @Override
    public PagesHelper createPagesData(PagedCatFactsDto _dto) {
        logger.debug(TAG, "createPagesData");
        return new PagesHelper(_dto.getCurrentPage(), _dto.getLastPage());
    }
}
