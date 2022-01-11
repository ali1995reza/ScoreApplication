package server

const (
	PathParamUserId        = "userId"
	PathParamApplicationId = "applicationId"

	QueryParamOffset = "offset"
	QueryParamSize   = "size"
	QueryParamTop    = "top"
	QueryParamBottom = "bottom"
	QueryParamUserId = "userId"

	HeaderClientToken = "X-CLIENT-TOKEN"

	UrlLogin           = "/login/:" + PathParamUserId
	UrlSubmitScore     = "/applications/:" + PathParamApplicationId + "/scores"
	UrlGetTopScoreList = "/applications/:" + PathParamApplicationId + "/scores"
	UrlSearchScoreList = "/applications/:" + PathParamApplicationId + "/scores/search"
)
