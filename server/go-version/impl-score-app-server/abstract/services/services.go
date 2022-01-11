package service

import "impl-score-app-server/abstract/exceptions"

type AuthenticationService interface {
	CreateToken(userId string, expireAfter int64) *string
	ValidateToken(token string) (*string, exceptions.ScoreApplicationException)
}
