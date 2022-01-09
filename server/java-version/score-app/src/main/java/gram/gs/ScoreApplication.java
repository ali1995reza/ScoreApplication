package gram.gs;

import gram.gs.assertion.Assert;
import gram.gs.exceptions.*;
import gram.gs.model.RankedScore;
import gram.gs.repository.ScoreRepository;
import gram.gs.repository.UserRepository;
import gram.gs.service.AuthenticationService;
import gram.gs.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScoreApplication {

    public final static Logger LOGGER = LoggerFactory.getLogger(ScoreApplication.class);

    private final UserRepository userRepository;
    private final ScoreRepository scoreRepository;
    private final AuthenticationService authenticationService;

    public ScoreApplication(UserRepository userRepository, ScoreRepository scoreRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.scoreRepository = scoreRepository;
        this.authenticationService = authenticationService;
    }

    public String login(final String userId) throws ScoreApplicationException {
        LOGGER.debug("login user with id {}", userId);
        //first validate user id
        Assert.isTrue(Utils.isValidUserId(userId), InvalidUserIdFormatException::new);
        userRepository.addOrGet(userId);
        return authenticationService.createToken(userId, 10, TimeUnit.MINUTES);
    }

    public RankedScore submitScore(final String token, final String applicationId, final long score) throws ScoreApplicationException {
        final String userId = authenticationService.validateToken(token);
        LOGGER.debug("submit score {} for application {} by user {}", score, applicationId, userId);
        //check if asserted user is really exists
        Assert.isNotNull(userRepository.get(userId), AuthenticationTokenInvalidException::new);
        //check if user id and application id is valid
        Assert.isTrue(Utils.isValidUserId(userId), InvalidUserIdFormatException::new);
        Assert.isTrue(Utils.isValidApplicationId(applicationId), InvalidApplicationIdFormatException::new);
        //check if score is negative which is invalid
        Assert.isNotNegative(score, () -> new InvalidParametersException("[score] parameter must greater equals than 0"));
        return scoreRepository.save(userId, applicationId, score);
    }

    public List<RankedScore> getTopScoreList(final String applicationId, final long offset, final long size) throws ScoreApplicationException {
        LOGGER.debug("get top score list for application {} with offset {} and size {}", applicationId, offset, size);
        //check if application id is valid
        Assert.isTrue(Utils.isValidApplicationId(applicationId), InvalidApplicationIdFormatException::new);
        Assert.isNotNegative(offset, () -> new InvalidParametersException("[offset] parameter must greater equals than 0"));
        Assert.isPositive(size, () -> new InvalidParametersException("[size] parameter must greater equals than 1"));
        return scoreRepository.get(applicationId, offset, size);
    }

    public List<RankedScore> searchScoreList(final String userId, final String applicationId, final int top, final int bottom) throws ScoreApplicationException {
        LOGGER.debug("search score list of application {} to find score of user {} - params : top={}, bottom={}", applicationId, userId, top, bottom);
        //check if user id and application id is valid
        Assert.isTrue(Utils.isValidUserId(userId), InvalidUserIdFormatException::new);
        Assert.isTrue(Utils.isValidApplicationId(applicationId), InvalidApplicationIdFormatException::new);
        Assert.isNotNegative(top, () -> new InvalidParametersException("[top] parameter must greater equals than 0"));
        Assert.isNotNegative(top, () -> new InvalidParametersException("[bottom] parameter must greater equals than 0"));
        List<RankedScore> scores = scoreRepository.get(userId, applicationId, top, bottom);
        Assert.isNotNull(scores, ScoreNotFoundException::new);
        return scores;
    }

    public RankedScore getUserScore(final String userId, final String applicationId) throws ScoreApplicationException {
        LOGGER.debug("get score for user {} in application {}", userId, applicationId);
        //check if user id and application id is valid
        Assert.isTrue(Utils.isValidUserId(userId), InvalidUserIdFormatException::new);
        Assert.isTrue(Utils.isValidApplicationId(applicationId), InvalidApplicationIdFormatException::new);
        RankedScore score = scoreRepository.get(userId, applicationId);
        Assert.isNotNull(score, ScoreNotFoundException::new);
        return score;
    }
}
