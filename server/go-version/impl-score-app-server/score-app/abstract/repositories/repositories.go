package repository

import (
	"impl-score-app-server/score-app/abstract/exceptions"
	"impl-score-app-server/score-app/abstract/models"
)

type UserRepository interface {
	Add(id string) (*models.User, *exceptions.UserAlreadyExistsException)
	Get(id string) *models.User
	AddOrGet(id string) *models.User
}

type ScoreRepository interface {
	Save(userId string, applicationId string, score int64) *models.RankedScore
	Get(userId string, applicationId string) *models.RankedScore
	GetTopScore(applicationId string, offset int64, size int64) []*models.RankedScore
	Search(userId string, applicationId string, top int32, bottom int32) []*models.RankedScore
}
