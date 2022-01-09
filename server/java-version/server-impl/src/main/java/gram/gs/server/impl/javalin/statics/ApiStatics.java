package gram.gs.server.impl.javalin.statics;

public class ApiStatics {

    public final static class PathParams {
        public final static String USER_ID = "userId";
        public final static String APPLICATION_ID = "applicationId";
    }

    public final static class QueryParams {
        public final static String OFFSET = "offset";
        public final static String SIZE = "size";
        public final static String TOP = "top";
        public final static String BOTTOM = "bottom";
        public final static String USER_ID = "userId";
    }

    public final static class Headers {
        public final static String CLIENT_TOKEN = "X-CLIENT-TOKEN";
    }

    public final static class Urls {
        public final static String LOGIN = "/login/{" + PathParams.USER_ID + "}";
        public final static String SUBMIT_SCORE = "/applications/{" + PathParams.APPLICATION_ID + "}/scores";
        public final static String GET_TOP_SCORES_LIST = "/applications/{" + PathParams.APPLICATION_ID + "}/scores";
        public final static String SEARCH_SCORES_LIST = "/applications/{" + PathParams.APPLICATION_ID + "}/scores/search";
    }

}
