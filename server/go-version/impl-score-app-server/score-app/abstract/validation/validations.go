package validation

import "regexp"

const (
	UserIdPattern        = "^([0-9a-zA-Z_\\-]){3,}$"
	ApplicationIdPattern = "^([0-9a-zA-Z_\\-]){3,}$"
)

type Validator struct {
	userIdRegx        *regexp.Regexp
	applicationIdRegx *regexp.Regexp
}

func NewValidator() *Validator {
	userIdRegx, _ := regexp.Compile(UserIdPattern)
	applicationIdRegx, _ := regexp.Compile(ApplicationIdPattern)
	return &Validator{userIdRegx: userIdRegx, applicationIdRegx: applicationIdRegx}
}

func (validator *Validator) IsValidUserId(userId string) bool {
	return validator.userIdRegx.MatchString(userId)
}

func (validator *Validator) IsValidApplicationId(applicationId string) bool {
	return validator.applicationIdRegx.MatchString(applicationId)
}
