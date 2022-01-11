package score_app

import (
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
}

func NewScoreApplication(authenticationService service.AuthenticationService, userRepository repository.UserRepository, scoreRepository repository.ScoreRepository) *ScoreApplication {
	return &ScoreApplication{authenticationService: authenticationService, userRepository: userRepository, scoreRepository: scoreRepository}
}

func (app *ScoreApplication) Login(userId string) (*string, exceptions.ScoreApplicationException) {
	if validation.IsValidUserId(userId) {
		return nil, exceptions.NewInvalidUserIdFormatException("user id format invalid")
	}
	app.userRepository.AddOrGet(userId)
	return app.authenticationService.CreateToken(userId, 10*10*1000), nil
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
	if validation.IsValidApplicationId(applicationId) {
		return nil, exceptions.NewInvalidApplicationIdFormatException("application id format invalid")
	}
	return app.scoreRepository.Save(*userId, applicationId, score), nil
}

func (app *ScoreApplication) GetTopScoreList(applicationId string, offset int64, size int64) ([]*models.RankedScore, exceptions.ScoreApplicationException) {
	if validation.IsValidApplicationId(applicationId) {
		return nil, exceptions.NewInvalidApplicationIdFormatException("application id format invalid")
	}
	return app.scoreRepository.GetTopScore(applicationId, offset, size), nil
}

func (app *ScoreApplication) Search(userId string, applicationId string, top int32, bottom int32) ([]*models.RankedScore, exceptions.ScoreApplicationException) {
	if validation.IsValidUserId(userId) {
		return nil, exceptions.NewInvalidUserIdFormatException("user id format invalid")
	}
	if validation.IsValidApplicationId(applicationId) {
		return nil, exceptions.NewInvalidApplicationIdFormatException("application id format invalid")
	}
	result := app.scoreRepository.Search(userId, applicationId, top, bottom)
	if result == nil {
		return nil, exceptions.NewScoreNotFoundException("requested score not found")
	}
	return result, nil
}
