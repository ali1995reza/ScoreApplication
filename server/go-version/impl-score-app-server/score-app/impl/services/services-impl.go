package jwt_auth_service

import (
	"github.com/golang-jwt/jwt/v4"
	"github.com/google/uuid"
	"impl-score-app-server/score-app/abstract/exceptions"
	"time"
)

const (
	ISSUER          = "issuer"
	USER_ID         = "userId"
	EXPIRATION_TIME = "expirationTime"
	RANDOM_VALUE    = "__rand__"
)

type JwtAuthenticationService struct {
	secret []byte
	method jwt.SigningMethod
}

func NewJwtAuthenticationService(secret string) *JwtAuthenticationService {
	return &JwtAuthenticationService{secret: []byte(secret), method: jwt.SigningMethodHS256}
}

func (j *JwtAuthenticationService) CreateToken(userId string, expireAfter int64) *string {
	token := jwt.NewWithClaims(j.method, jwt.MapClaims{
		ISSUER:          "GramGames",
		USER_ID:         userId,
		EXPIRATION_TIME: time.Now().UnixMilli() + expireAfter,
		RANDOM_VALUE:    random(),
	})

	// Sign and get the complete encoded token as a string using the secret
	tokenString, _ := token.SignedString(j.secret)

	return &tokenString
}

func (j JwtAuthenticationService) ValidateToken(tokenString string) (*string, exceptions.ScoreApplicationException) {
	token, err := jwt.Parse(tokenString, func(jwtToken *jwt.Token) (interface{}, error) {
		return j.secret, nil
	})
	if err != nil || !token.Valid {
		return nil, exceptions.NewAuthenticationTokenInvalidException("invalid token")
	}
	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return nil, exceptions.NewAuthenticationTokenInvalidException("invalid token")
	}
	userId, ok := claims[USER_ID].(string)
	if !ok {
		return nil, exceptions.NewAuthenticationTokenInvalidException("invalid token")
	}
	issuer, ok := claims[ISSUER].(string)
	if !ok || issuer != "GramGames" {
		return nil, exceptions.NewAuthenticationTokenInvalidException("invalid token")
	}
	expirationTime := claims[EXPIRATION_TIME].(float64)
	if !ok {
		return nil, exceptions.NewAuthenticationTokenInvalidException("invalid token")
	}
	if time.Now().UnixMilli() > int64(expirationTime) {
		return nil, exceptions.NewAuthenticationTokenExpiredException("token expired")
	}
	return &userId, nil
}

func random() string {
	uid, _ := uuid.NewUUID()
	return uid.String()
}
