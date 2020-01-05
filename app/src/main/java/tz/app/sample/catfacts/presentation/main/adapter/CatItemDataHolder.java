package tz.app.sample.catfacts.presentation.main.adapter;

import java.util.Objects;

import tz.app.sample.catfacts.domain.model.CatFact;
import tz.app.sample.catfacts.domain.model.CatImg;

public final class CatItemDataHolder {

    final CatImg catImg;
    final CatFact catFact;

    private ICatItemClickListener catItemClickListener;

    public CatItemDataHolder(final CatImg _catImg, final CatFact _catFact) {
        catImg = _catImg;
        catFact = _catFact;
    }

    final ICatItemClickListener getCatItemClickListener() {
        return catItemClickListener;
    }

    public final void setCatItemClickListener(final ICatItemClickListener _catItemClickListener) {
        catItemClickListener = _catItemClickListener;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CatItemDataHolder that = (CatItemDataHolder) o;
        return Objects.equals(catImg, that.catImg) && Objects.equals(catFact, that.catFact);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(catImg, catFact);
    }
}
