package tzpace.app.catfactssample.domain.model;

public final class CatImg {

    public final String imgUrl;

    public CatImg(final String _imgUrl) {
        imgUrl = _imgUrl;
    }

    @Override
    public final String toString() {
        return "CatImg | " + imgUrl;
    }

}
