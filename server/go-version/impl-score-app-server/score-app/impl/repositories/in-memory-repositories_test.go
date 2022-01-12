package in_memory_repositories

import (
	"math"
	"strconv"
	"testing"
)

func TestSubmitScore(test *testing.T) {

	repo := NewInMemoryScoreRepository()

	for i := 0; i < 1000; i++ {
		score := repo.Save(strconv.Itoa(i), "app", int64(i+1))
		if score.Rank != 1 {
			test.Errorf("expected 1 but got %d", score.Rank)
		}
	}
}

func TestSubmitScoreTwice(test *testing.T) {

	repo := NewInMemoryScoreRepository()

	userId := "user"
	applicationId := "app"
	score := repo.Save(userId, applicationId, 20)
	if score.Rank != 1 {
		test.Errorf("expected 1 but got %d", score.Rank)
	}
	score = repo.Save("anotherUser", applicationId, 30)
	if score.Rank != 1 {
		test.Errorf("expected 1 but got %q", score.Rank)
	}
	score = repo.Get(userId, applicationId)
	if score.Rank != 2 {
		test.Errorf("expected 2 but got %d", score.Rank)
	}
	score = repo.Save(userId, applicationId, 40)
	if score.Rank != 1 {
		test.Errorf("expected 1 but got %d", score.Rank)
	}
}

func TestGetScoreList(test *testing.T) {
	repo := NewInMemoryScoreRepository()

	for i := 0; i < 1000; i++ {
		score := repo.Save(strconv.Itoa(i), strconv.Itoa(i/100), int64(math.Mod(float64(i), 100)+1))
		if score.Rank != 1 {
			test.Errorf("expected 1 but got %d", score.Rank)
		}
	}

	for i := 0; i < 10; i++ {
		scores := repo.GetTopScore(strconv.Itoa(i), 10, 20)
		count := 0
		for _, score := range scores {

			if score.Score != int64(100-10-count) {
				test.Errorf("expect Score %d but got %d", (100 - 10 - count), score.Score)
			}
			if !(score.Rank == int64(10+1+count)) {
				test.Errorf("expect Rank %d but got %d", (10 + 1 + count), score.Rank)
			}
			if score.UserId != strconv.Itoa(((i+1)*100)-1-10-count) {
				test.Errorf("expect UserId %s but got %s", strconv.Itoa(((i+1)*100)-1-10-count), score.UserId)
			}
			count++
		}
	}
}

func TestSearchScoreList(test *testing.T) {
	repo := NewInMemoryScoreRepository()

	for i := 0; i < 1000; i++ {
		score := repo.Save(strconv.Itoa(i), "app", int64(i))
		if score.Rank != 1 {
			test.Errorf("expected 1 but got %d", score.Rank)
		}
	}
	scores := repo.Search(strconv.Itoa(999), "app", 1000, 1)
	if len(scores) != 2 {
		test.Errorf("invalid range of search")
	}
	if scores[0].UserId != "999" || scores[0].Rank != 1 {
		test.Errorf("expected user 999 has rank 1")
	}
	if scores[1].UserId != "998" || scores[1].Rank != 2 {
		test.Errorf("expected user 998 has rank 2")
	}
	scores = repo.Search(strconv.Itoa(0), "app", 1, 1000)
	if len(scores) != 2 {
		test.Errorf("search list size must be 2 but got %d", len(scores))
	}
	if scores[0].UserId != "1" || scores[0].Rank != 999 {
		test.Errorf("expected user 1 has rank 999")
	}
	if scores[1].UserId != "0" || scores[1].Rank != 1000 {
		test.Errorf("expected user 0 has rank 1000")
	}
	scores = repo.Search(strconv.Itoa(200), "app", 1, 1)
	if len(scores) != 3 {
		test.Errorf("search list size must be 3 but got %d", len(scores))
	}
}

func TestAddUser(test *testing.T) {
	repo := NewInMemoryUserRepository()
	if user, ex := repo.Add("1"); ex != nil {
		test.Errorf(ex.GetMessage())
	} else if user.Id != "1" {
		test.Errorf("expected user id %s but got %s", "1", user.Id)
	}
}

func TestAddUserTwice(test *testing.T) {
	repo := NewInMemoryUserRepository()
	if _, ex := repo.Add("1"); ex != nil {
		test.Errorf(ex.GetMessage())
	}
	if _, ex := repo.Add("1"); ex == nil {
		test.Errorf("expect exception but got nothing")
	}
}

func TestAddOrGetUser(test *testing.T) {
	repo := NewInMemoryUserRepository()
	if _, ex := repo.Add("1"); ex != nil {
		test.Errorf(ex.GetMessage())
	}
	if user := repo.AddOrGet("1"); user.Id != "1" {
		test.Errorf("expected user id %s but got %s", "1", user.Id)
	}
}

func TestUserModelChangeSafety(test *testing.T) {
	repo := NewInMemoryUserRepository()
	if user, ex := repo.Add("1"); ex != nil {
		test.Errorf(ex.GetMessage())
	} else {
		user.Id = "10"
	}
	if user := repo.Get("1"); user.Id != "1" {
		test.Errorf("expected user id %s but got %s", "1", user.Id)
	}
}
