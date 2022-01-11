package in_memory_repositories

import (
	"impl-score-app-server/score-app/abstract/exceptions"
	"impl-score-app-server/score-app/abstract/models"
	"sync"
)

type InMemoryUserRepository struct {
	users sync.Map
}

func NewInMemoryUserRepository() *InMemoryUserRepository {
	repo := new(InMemoryUserRepository)
	repo.users = sync.Map{}
	return repo
}

func (repo *InMemoryUserRepository) Add(id string) (*models.User, *exceptions.UserAlreadyExistsException) {
	user := models.NewUser(id)
	_, loaded := repo.users.LoadOrStore(id, user)
	if loaded {
		return nil, exceptions.NewUserAlreadyExistsException("user [" + id + "] already exists")
	}
	return models.CopyUser(user), nil
}

func (repo *InMemoryUserRepository) Get(id string) *models.User {
	user, ok := repo.users.Load(id)
	if !ok {
		return nil
	}
	return user.(*models.User)
}

func (repo *InMemoryUserRepository) AddOrGet(id string) *models.User {
	u := models.NewUser(id)
	user, _ := repo.users.LoadOrStore(id, u)
	return models.CopyUser(user.(*models.User))
}

type InMemoryScoreRepository struct {
	userAppIndex   sync.Map
	appScoresIndex sync.Map
}

func NewInMemoryScoreRepository() *InMemoryScoreRepository {
	repo := new(InMemoryScoreRepository)
	repo.userAppIndex = sync.Map{}
	repo.appScoresIndex = sync.Map{}
	return repo
}

func (repo *InMemoryScoreRepository) Save(userId string, applicationId string, score int64) *models.RankedScore {
	sh, _ := repo.userAppIndex.LoadOrStore(createUserAppKey(userId, applicationId), &_ScoreHolder{Id: "", UserId: userId, ApplicationId: applicationId, Score: score})
	ls, _ := repo.appScoresIndex.LoadOrStore(applicationId, &_SortScoreList{Lock: sync.Mutex{}})
	scoreHolder := sh.(*_ScoreHolder)
	scoresList := ls.(*_SortScoreList)
	var result *models.RankedScore
	scoresList.Lock.Lock()
	if scoreHolder.Score <= score {
		index := scoresList.binarySearch(scoreHolder)
		if index >= 0 {
			scoresList.remove(index)
		}
		scoreHolder.Score = score
		index = -scoresList.binarySearch(scoreHolder) - 1
		scoresList.add(index, scoreHolder)
		result = convert(scoreHolder, int64(index+1))
	} else {
		rank := scoresList.binarySearch(scoreHolder)
		result = convert(scoreHolder, int64(rank+1))
	}
	scoresList.Lock.Unlock()
	return result
}

func (repo *InMemoryScoreRepository) Get(userId string, applicationId string) *models.RankedScore {
	sh, _ := repo.userAppIndex.Load(createUserAppKey(userId, applicationId))
	if sh == nil {
		return nil
	}
	ls, _ := repo.appScoresIndex.LoadOrStore(applicationId, &_SortScoreList{Lock: sync.Mutex{}})
	scoresList := ls.(*_SortScoreList)
	scoresList.Lock.Lock()
	scoreHolder := sh.(*_ScoreHolder)
	rank := scoresList.binarySearch(scoreHolder)
	scoresList.Lock.Unlock()
	return convert(scoreHolder, int64(rank+1))
}

func (repo *InMemoryScoreRepository) GetTopScore(applicationId string, offset int64, size int64) []*models.RankedScore {
	ls, _ := repo.appScoresIndex.LoadOrStore(applicationId, &_SortScoreList{Lock: sync.Mutex{}})
	scoresList := ls.(*_SortScoreList)
	scoresList.Lock.Lock()
	len := scoresList.possibleSize(offset, size)
	result := make([]*models.RankedScore, len)
	for i := int32(0); i < len; i++ {
		result[i] = convert(scoresList.Scores[i+int32(offset)], int64(i)+offset+1)
	}
	scoresList.Lock.Unlock()
	return result
}

func (repo *InMemoryScoreRepository) Search(userId string, applicationId string, top int32, bottom int32) []*models.RankedScore {
	sh, _ := repo.userAppIndex.Load(createUserAppKey(userId, applicationId))
	if sh == nil {
		return nil
	}
	ls, _ := repo.appScoresIndex.LoadOrStore(applicationId, &_SortScoreList{Lock: sync.Mutex{}})
	scoreHolder := sh.(*_ScoreHolder)
	scoresList := ls.(*_SortScoreList)
	scoresList.Lock.Lock()
	index := scoresList.binarySearch(scoreHolder)
	offset := max(index-top, 0)
	size := max(index-offset+bottom, int32(len(scoresList.Scores)))
	len := scoresList.possibleSize(int64(offset), int64(size))
	result := make([]*models.RankedScore, len)
	for i := int32(0); i < len; i++ {
		result[i] = convert(scoresList.Scores[i+offset], int64(i+offset+1))
	}
	scoresList.Lock.Unlock()
	return result
}

type _ScoreHolder struct {
	Id            string
	UserId        string
	ApplicationId string
	Score         int64
}

func (holder *_ScoreHolder) compareTo(other *_ScoreHolder) int {
	if holder.Score > other.Score {
		return 1
	}
	if holder.Score < other.Score {
		return -1
	}
	if holder.UserId > other.UserId {
		return 1
	}
	if holder.UserId < other.UserId {
		return -1
	}
	return 0
}

type _SortScoreList struct {
	Lock   sync.Mutex
	Scores []*_ScoreHolder
}

func createUserAppKey(userId string, applicationId string) string {
	return userId + "#" + applicationId
}

func convert(scoreHolder *_ScoreHolder, rank int64) *models.RankedScore {
	return models.NewRankedScore(scoreHolder.Id, scoreHolder.UserId, scoreHolder.ApplicationId, scoreHolder.Score, rank)
}

func (list *_SortScoreList) binarySearch(holder *_ScoreHolder) int32 {
	low := int32(0)
	high := int32(len(list.Scores) - 1)
	for low <= high {
		mid := int32(low+high) / 2
		midVal := list.Scores[mid]
		compareResult := -midVal.compareTo(holder) //DESC
		if compareResult == -1 {
			low = mid + 1
		} else if compareResult == 1 {
			high = mid - 1
		} else {
			return mid
		}
	}
	return -(low + 1)
}

func (list *_SortScoreList) remove(index int32) {
	list.Scores = append(list.Scores[:index], list.Scores[index+1:]...)
}

func (list *_SortScoreList) add(index int32, score *_ScoreHolder) {
	if len(list.Scores) == 0 || index == 0 {
		list.Scores = append([]*_ScoreHolder{score}, list.Scores...)
		list.Scores = append(list.Scores[:index], list.Scores[index:]...)
	} else {
		list.Scores = append(list.Scores[:index], list.Scores[index-1:]...)
	}
	list.Scores[index] = score
}

func (list *_SortScoreList) possibleSize(offset int64, size int64) int32 {
	totalLen := int64(len(list.Scores))
	if offset+size > totalLen {
		return max(int32(totalLen-offset), 0)
	} else {
		return int32(size)
	}
}

func max(a int32, b int32) int32 {
	if a > b {
		return a
	}
	return b
}
