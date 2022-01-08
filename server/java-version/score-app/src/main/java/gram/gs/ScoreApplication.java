package gram.gs;

import gram.gs.assertion.Assert;
import gram.gs.exceptions.*;
import gram.gs.model.RankedScore;
import gram.gs.repository.ScoreRepository;
import gram.gs.repository.UserRepository;
import gram.gs.service.AuthenticationService;
import gram.gs.util.Utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScoreApplication {

    private final UserRepository userRepository;
    private final ScoreRepository scoreRepository;
    private final AuthenticationService authenticationService;

    public ScoreApplication(UserRepository userRepository, ScoreRepository scoreRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.scoreRepository = scoreRepository;
        this.authenticationService = authenticationService;
    }

    public String login(String userId) throws ScoreApplicationException {
        Assert.isTrue(Utils.isValidUserId(userId), InvalidUserIdFormatException::new);
        userRepository.addOrGet(userId);
        return authenticationService.createToken(userId, 10, TimeUnit.MINUTES);
    }

    public RankedScore submitScore(String token, String applicationId, Integer score) throws ScoreApplicationException {
        String userId = authenticationService.validateToken(token);
        Assert.isNotNull(userRepository.get(userId), AuthenticationTokenInvalidException::new);
        Assert.isNotNegative(score, () -> new InvalidParametersException("Score parameter must greater equals than 0"));
        return scoreRepository.save(userId, applicationId, score);
    }

    public List<RankedScore> getTopScoreList(String applicationId, Long offset, Long size) {
        return scoreRepository.get(applicationId, offset, size);
    }

    public List<RankedScore> searchScoreList(String userId, String applicationId, int top, int bottom) throws ScoreApplicationException {
        List<RankedScore> scores = scoreRepository.get(userId, applicationId, top, bottom);
        Assert.isNotNull(scores, ScoreNotFoundException::new);
        return scores;
    }

    public RankedScore getUserScore(String userId, String applicationId) throws ScoreApplicationException {
        RankedScore score = scoreRepository.get(userId, applicationId);
        Assert.isNotNull(score, ScoreNotFoundException::new);
        return score;
    }
}
