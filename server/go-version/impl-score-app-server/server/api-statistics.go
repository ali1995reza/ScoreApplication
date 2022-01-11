package server

const (
	PATH_PARAM_USER_ID        = "userId"
	PATH_PARAM_APPLICATION_ID = "applicationId"

	QUERY_PARAM_OFFSET  = "offset"
	QUERY_PARAM_SIZE    = "size"
	QUERY_PARAM_TOP     = "top"
	QUERY_PARAM_BOTTOM  = "bottom"
	QUERY_PARAM_USER_ID = "userId"

	HEADER_CLIENT_TOKEN = "X-CLIENT-TOKEN"

	URL_LOGIN              = "/login/:" + PATH_PARAM_USER_ID
	URL_SUBMIT_SCORE       = "/applications/:" + PATH_PARAM_APPLICATION_ID + "/scores"
	URL_GET_TOP_SCORE_LIST = "/applications/:" + PATH_PARAM_APPLICATION_ID + "/scores"
	URL_SEARCH_SCORE_LIST  = "/applications/:" + PATH_PARAM_APPLICATION_ID + "/scores/search"
)
