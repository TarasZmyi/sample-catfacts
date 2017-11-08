package tzpace.app.catfactssample.domain.model;

public final class CatFact {

    public final String factTxt;

    public CatFact(final String _factTxt) {
        factTxt = _factTxt;
    }

    @Override
    public final String toString() {
        return "CatFact | " + factTxt;
    }

}
