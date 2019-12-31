package tz.app.sample.catfacts.communication;

public final class ApiConst {

    public static final int CONN_TIMEOUT = 15; //SECONDS

    public static final int PAGE_SIZE = 20; //ITEMS

    static final String BASE_URL_CAT_FACT = "https://catfact.ninja";

    static final String BASE_URL_CAT_IMG = "https://api.thecatapi.com";

    private ApiConst() {
    }

    public final class Path {

        public static final String GET_FACTS = "/facts";
        public static final String GET_FACT = "/factTxt";

        public static final String GET_IMAGES = "/v1/images/search";

        private Path() {
        }
    }

    public final class QueryParam {

        public static final String INT_MAX_LENGTH = "max_length";
        public static final String INT_LIMIT = "limit";
        public static final String INT_PAGE = "page";

        private QueryParam() {
        }
    }

}
