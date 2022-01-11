package server

import (
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	score_app "impl-score-app-server/score-app"
	"impl-score-app-server/score-app/abstract/exceptions"
	"net/http"
	"strconv"
)

type ScoreApplicationServer struct {
	address     string
	application *score_app.ScoreApplication
}

func NewScoreApplicationServer(address string, application *score_app.ScoreApplication) *ScoreApplicationServer {
	return &ScoreApplicationServer{address: address, application: application}
}

func (server *ScoreApplicationServer) Run() {
	gin.SetMode(gin.DebugMode)
	router := gin.Default()
	router.GET(URL_LOGIN, func(context *gin.Context) {
		userId := context.Param(PATH_PARAM_USER_ID)
		token, error := server.application.Login(userId)
		if error != nil {
			context.JSON(http.StatusBadRequest, errorResponseFrom(error))
		} else {
			context.JSON(http.StatusOK, ClientToken{Token: *token})
		}
	})
	router.GET(URL_GET_TOP_SCORE_LIST, func(context *gin.Context) {
		applicationId := context.Param(PATH_PARAM_APPLICATION_ID)
		offset, parseErr := strconv.ParseInt(context.Query(QUERY_PARAM_OFFSET), 10, 64)
		if parseErr != nil {
			context.JSON(http.StatusBadRequest, errorResponseFrom(exceptions.NewInvalidParametersException("parameter ["+QUERY_PARAM_OFFSET+"] is invalid or not exists")))
			return
		}
		size, parseErr := strconv.ParseInt(context.Query(QUERY_PARAM_SIZE), 10, 64)
		if parseErr != nil {
			context.JSON(http.StatusBadRequest, errorResponseFrom(exceptions.NewInvalidParametersException("parameter ["+QUERY_PARAM_SIZE+"] is invalid or not exists")))
			return
		}
		result, err := server.application.GetTopScoreList(applicationId, offset, size)
		if err != nil {
			context.JSON(http.StatusBadRequest, errorResponseFrom(err))
			return
		}
		context.JSON(http.StatusOK, result)
	})
	router.PUT(URL_SUBMIT_SCORE, func(context *gin.Context) {
		applicationId := context.Param(PATH_PARAM_APPLICATION_ID)
		token := context.GetHeader(HEADER_CLIENT_TOKEN)
		var request SubmitScoreRequest
		jsonParseErr := context.BindJSON(&request)
		if jsonParseErr != nil {
			context.JSON(http.StatusBadRequest, exceptions.NewInvalidParametersException("invalid json format for body"))
			return
		}
		result, err := server.application.SubmitScore(token, applicationId, request.Score)
		if err != nil {
			context.JSON(http.StatusBadRequest, errorResponseFrom(err))
		}
		context.JSON(http.StatusOK, result)
	})
	router.GET(URL_SEARCH_SCORE_LIST, func(context *gin.Context) {
		applicationId := context.Param(PATH_PARAM_APPLICATION_ID)
		top, parseErr := strconv.ParseInt(context.Query(QUERY_PARAM_TOP), 10, 64)
		if parseErr != nil {
			context.JSON(http.StatusBadRequest, errorResponseFrom(exceptions.NewInvalidParametersException("parameter ["+QUERY_PARAM_TOP+"] is invalid or not exists")))
			return
		}
		bottom, parseErr := strconv.ParseInt(context.Query(QUERY_PARAM_BOTTOM), 10, 64)
		if parseErr != nil {
			context.JSON(http.StatusBadRequest, errorResponseFrom(exceptions.NewInvalidParametersException("parameter ["+QUERY_PARAM_BOTTOM+"] is invalid or not exists")))
			return
		}
		userId := context.Query(QUERY_PARAM_USER_ID)
		result, err := server.application.Search(userId, applicationId, int32(top), int32(bottom))
		if err != nil {
			if _, ok := err.(*exceptions.ScoreNotFoundException); ok {
				context.JSON(http.StatusNotFound, errorResponseFrom(err))
				return
			}
			context.JSON(http.StatusBadRequest, errorResponseFrom(err))
		}
		context.JSON(http.StatusOK, result)
	})
	err := router.Run(server.address)
	print(err.Error())
}

type ClientToken struct {
	Token string `json:"token"`
}

type SubmitScoreRequest struct {
	Score int64 `json:"score"`
}

type ErrorResponse struct {
	Id      string `json:"id"`
	Message string `json:"message"`
	TraceId string `json:"traceId"`
}

func errorResponseFrom(ex exceptions.ScoreApplicationException) *ErrorResponse {
	uid, _ := uuid.NewUUID()
	return &ErrorResponse{Id: ex.GetId(), Message: ex.GetMessage(), TraceId: uid.String()}
}
