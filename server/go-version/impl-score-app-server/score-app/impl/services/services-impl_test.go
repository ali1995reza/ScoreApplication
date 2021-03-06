package jwt_auth_service

import (
	"testing"
	"time"
)

func TestCreateToken(test *testing.T) {
	service := NewJwtAuthenticationService("secret")
	if _, ok := service.CreateToken("user", 10); !ok {
		test.Errorf("can not create token string")
	}
}

func TestValidateToken(test *testing.T) {
	service := NewJwtAuthenticationService("secret")
	tokeString, ok := service.CreateToken("user", 1000000)
	if !ok {
		test.Errorf("can not create token string")
	}
	if userId, ex := service.ValidateToken(tokeString); ex != nil {
		test.Errorf(ex.GetMessage())
	} else if userId != "user" {
		test.Errorf("expected user id %s but got %s", "1", userId)
	}
}

func TestTokenExpiredException(test *testing.T) {
	service := NewJwtAuthenticationService("secret")
	tokeString, _ := service.CreateToken("user", 1)
	time.Sleep(time.Millisecond * 2)
	if _, ex := service.ValidateToken(tokeString); ex == nil {
		test.Errorf("expect got exception but got nothing")
	}
}

func TestInvalidTokenException(test *testing.T) {
	service := NewJwtAuthenticationService("secret")
	time.Sleep(time.Millisecond * 2)
	if _, ex := service.ValidateToken("invalid-token"); ex == nil {
		test.Errorf("expect got exception but got nothing")
	}
}
