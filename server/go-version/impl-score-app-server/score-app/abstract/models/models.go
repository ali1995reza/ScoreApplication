package models

type User struct {
	Id string `json:"id"`
}

func NewUser(id string) *User {
	return &User{Id: id}
}

func CopyUser(user *User) *User {
	return &User{Id: user.Id}
}

type RankedScore struct {
	Id            string `json:"id"`
	UserId        string `json:"userId"`
	ApplicationId string `json:"applicationId"`
	Score         int64  `json:"score"`
	Rank          int64  `json:"rank"`
}

func NewRankedScore(id string, userId string, applicationId string, score int64, rank int64) *RankedScore {
	return &RankedScore{Id: id, UserId: userId, ApplicationId: applicationId, Score: score, Rank: rank}
}
