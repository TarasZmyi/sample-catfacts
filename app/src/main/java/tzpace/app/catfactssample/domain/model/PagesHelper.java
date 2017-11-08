package tzpace.app.catfactssample.domain.model;

public final class PagesHelper {

    private final int currentPage;

    private final int lastPage;

    public PagesHelper(int _currentPage, int _lastPage) {
        currentPage = _currentPage;
        lastPage = _lastPage;
    }

    public final boolean isLastPage() {
        return currentPage == lastPage;
    }

    public final int getNextPageInt() {
        return currentPage + 1;
    }

}
