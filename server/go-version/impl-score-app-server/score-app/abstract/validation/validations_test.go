package validation

import "testing"

func TestValidateUserId(test *testing.T) {
	validator := NewValidator()
	if !validator.IsValidUserId("123") {
		test.Errorf("expect valid user-id but not")
	}
	if !validator.IsValidUserId("john") {
		test.Errorf("expect valid user-id but not")
	}
	if !validator.IsValidUserId("john-123") {
		test.Errorf("expect valid user-id but not")
	}
	if !validator.IsValidUserId("john_123") {
		test.Errorf("expect valid user-id but not")
	}
	if validator.IsValidUserId("%john_12") {
		test.Errorf("expect invalid user-id but valid")
	}
	if validator.IsValidUserId("john 223") {
		test.Errorf("expect invalid user-id but valid")
	}
	if validator.IsValidUserId("john#223") {
		test.Errorf("expect invalid user-id but valid")
	}
	if validator.IsValidUserId("john.123") {
		test.Errorf("expect invalid user-id but valid")
	}
}

func TestValidateApplicationId(test *testing.T) {
	validator := NewValidator()
	if !validator.IsValidUserId("123") {
		test.Errorf("expect valid user-id but not")
	}
	if !validator.IsValidUserId("app") {
		test.Errorf("expect valid user-id but not")
	}
	if !validator.IsValidUserId("app-123") {
		test.Errorf("expect valid user-id but not")
	}
	if !validator.IsValidUserId("app_123") {
		test.Errorf("expect valid user-id but not")
	}
	if validator.IsValidUserId("%app_12") {
		test.Errorf("expect invalid user-id but valid")
	}
	if validator.IsValidUserId("app 223") {
		test.Errorf("expect invalid user-id but valid")
	}
	if validator.IsValidUserId("app#223") {
		test.Errorf("expect invalid user-id but valid")
	}
	if validator.IsValidUserId("app.123") {
		test.Errorf("expect invalid user-id but valid")
	}
}
