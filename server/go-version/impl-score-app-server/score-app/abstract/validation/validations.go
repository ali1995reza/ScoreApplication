package validation

import "regexp"

const (
	USER_ID_PATTERN        = "^([0-9a-zA-Z_\\-]){3,}$"
	APPLICATION_ID_PATTERN = "^([0-9a-zA-Z_\\-]){3,}$"
)

type Validator struct {
	userIdRegx        *regexp.Regexp
	applicationIdRegx *regexp.Regexp
}

func NewValidator() *Validator {
	userIdRegx, _ := regexp.Compile(USER_ID_PATTERN)
	applicationIdRegx, _ := regexp.Compile(APPLICATION_ID_PATTERN)
	return &Validator{userIdRegx: userIdRegx, applicationIdRegx: applicationIdRegx}
}

func (validator *Validator) IsValidUserId(userId string) bool {
	return validator.userIdRegx.MatchString(userId)
}

func (validator *Validator) IsValidApplicationId(applicationId string) bool {
	return validator.applicationIdRegx.MatchString(applicationId)
}
