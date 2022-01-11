package validation

import "regexp"

const (
	USER_ID_PATTERN        = "^([0-9a-zA-Z_\\-]{3,})+$"
	APPLICATION_ID_PATTERN = "^([0-9a-zA-Z_\\-]{3,})+$"
)

func IsValidUserId(userId string) bool {
	regx, _ := regexp.Compile(USER_ID_PATTERN)
	return regx.MatchString(userId)
}

func IsValidApplicationId(applicationId string) bool {
	regx, _ := regexp.Compile(APPLICATION_ID_PATTERN)
	return regx.MatchString(applicationId)
}
