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
			test.Errorf("expected 1 but got %q", score.Rank)
		}
	}
}

func TestSubmitScoreTwice(test *testing.T) {

	repo := NewInMemoryScoreRepository()

	userId := "user"
	applicationId := "app"
	score := repo.Save(userId, applicationId, 20)
	if score.Rank != 1 {
		test.Errorf("expected 1 but got %q", score.Rank)
	}
	score = repo.Save("anotherUser", applicationId, 30)
	if score.Rank != 1 {
		test.Errorf("expected 1 but got %q", score.Rank)
	}
	score = repo.Get(userId, applicationId)
	if score.Rank != 2 {
		test.Errorf("expected 2 but got %q", score.Rank)
	}
	score = repo.Save(userId, applicationId, 40)
	if score.Rank != 1 {
		test.Errorf("expected 1 but got %q", score.Rank)
	}
}

func TestGetScoreList(test *testing.T) {
	repo := NewInMemoryScoreRepository()

	for i := 0; i < 1000; i++ {
		score := repo.Save(strconv.Itoa(i), strconv.Itoa(i/100), int64(math.Mod(float64(i), 100)+1))
		if score.Rank != 1 {
			test.Errorf("expected 1 but got %q", score.Rank)
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
				test.Errorf("expect UserId %s but got %q", strconv.Itoa(((i+1)*100)-1-10-count), score.UserId)
			}
			count++
		}
	}
}
