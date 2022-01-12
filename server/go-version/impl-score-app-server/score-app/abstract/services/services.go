package service

import (
	"impl-score-app-server/score-app/abstract/exceptions"
)

type AuthenticationService interface {
	CreateToken(userId string, expireAfter int64) (string, bool)
	ValidateToken(token string) (string, exceptions.ScoreApplicationException)
}
