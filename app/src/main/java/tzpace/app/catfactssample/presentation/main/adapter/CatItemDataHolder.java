package tzpace.app.catfactssample.presentation.main.adapter;

import tzpace.app.catfactssample.domain.model.CatFact;
import tzpace.app.catfactssample.domain.model.CatImg;

public final class CatItemDataHolder {

    public CatFact catFact;

    public CatImg catImg;

    private ICatItemClickListener catItemClickListener;

    public CatItemDataHolder() {
        super();
    }

    final ICatItemClickListener getCatItemClickListener() {
        return catItemClickListener;
    }

    public final void setCatItemClickListener(final ICatItemClickListener _catItemClickListener) {
        catItemClickListener = _catItemClickListener;
    }
}
