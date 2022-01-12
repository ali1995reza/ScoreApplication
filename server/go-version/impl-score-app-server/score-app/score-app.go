package score_app

import (
	"fmt"
	"impl-score-app-server/score-app/abstract/exceptions"
	"impl-score-app-server/score-app/abstract/models"
	repository "impl-score-app-server/score-app/abstract/repositories"
	service "impl-score-app-server/score-app/abstract/services"
	"impl-score-app-server/score-app/abstract/validation"
)

type ScoreApplication struct {
	authenticationService service.AuthenticationService
	userRepository        repository.UserRepository
	scoreRepository       repository.ScoreRepository
	validator             validation.Validator
}

func NewScoreApplication(authenticationService service.AuthenticationService, userRepository repository.UserRepository, scoreRepository repository.ScoreRepository) *ScoreApplication {
	return &ScoreApplication{authenticationService: authenticationService, userRepository: userRepository, scoreRepository: scoreRepository, validator: *validation.NewValidator()}
}

func (app *ScoreApplication) Login(userId string) (*string, exceptions.ScoreApplicationException) {
	fmt.Printf("login user with id %s\n", userId)
	if !app.validator.IsValidUserId(userId) {
		return nil, exceptions.NewInvalidUserIdFormatException("user id format invalid")
	}
	app.userRepository.AddOrGet(userId)
	return app.authenticationService.CreateToken(userId, 10*60*1000), nil
}

func (app *ScoreApplication) SubmitScore(token string, applicationId string, score int64) (*models.RankedScore, exceptions.ScoreApplicationException) {
	userId, ex := app.authenticationService.ValidateToken(token)
	if ex != nil {
		return nil, ex
	}
	user := app.userRepository.Get(*userId)
	if user == nil {
		return nil, exceptions.NewAuthenticationTokenInvalidException("user not found")
	}
	if score < 0 {
		return nil, exceptions.NewInvalidParametersException("[score] cant not be negative")
	}
	if !app.validator.IsValidApplicationId(applicationId) {
		return nil, exceptions.NewInvalidApplicationIdFormatException("application id format invalid")
	}
	fmt.Printf("submit score %d for application %s by user %s\n", score, applicationId, *userId)
	return app.scoreRepository.Save(*userId, applicationId, score), nil
}

func (app *ScoreApplication) GetTopScoreList(applicationId string, offset int64, size int64) ([]*models.RankedScore, exceptions.ScoreApplicationException) {
	fmt.Printf("get top score list for application %s with offset %d and size %d\n", applicationId, offset, size)
	if !app.validator.IsValidApplicationId(applicationId) {
		return nil, exceptions.NewInvalidApplicationIdFormatException("application id format invalid")
	}
	if offset < 0 {
		return nil, exceptions.NewInvalidParametersException("[offset] can not be negative")
	}
	if size < 1 {
		return nil, exceptions.NewInvalidParametersException("[size] must be grater than equal 1")
	}
	return app.scoreRepository.GetTopScore(applicationId, offset, size), nil
}

func (app *ScoreApplication) Search(userId string, applicationId string, top int32, bottom int32) ([]*models.RankedScore, exceptions.ScoreApplicationException) {
	fmt.Printf("search score list of application %s to find score of user %s - params : top=%d, bottom=%d\n", applicationId, userId, top, bottom)
	if !app.validator.IsValidUserId(userId) {
		return nil, exceptions.NewInvalidUserIdFormatException("user id format invalid")
	}
	if !app.validator.IsValidApplicationId(applicationId) {
		return nil, exceptions.NewInvalidApplicationIdFormatException("application id format invalid")
	}
	if top < 0 {
		return nil, exceptions.NewInvalidParametersException("[top] can not be negative")
	}
	if bottom < 0 {
		return nil, exceptions.NewInvalidParametersException("[bottom] can not be negative")
	}
	result := app.scoreRepository.Search(userId, applicationId, top, bottom)
	if result == nil {
		return nil, exceptions.NewScoreNotFoundException("requested score not found")
	}
	return result, nil
}
