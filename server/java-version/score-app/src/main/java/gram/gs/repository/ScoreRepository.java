package gram.gs.repository;

import gram.gs.model.RankedScore;

import java.util.List;

public interface ScoreRepository {

    /**
     * Save score of user for specific application. If the score less than last
     * submitted score this method just return higher score and will not save new score
     *
     * @param userId        user identifier
     * @param applicationId application identifier
     * @param score         score
     * @return a {@link RankedScore} instance which represent user score and current rank in rank list
     */
    RankedScore save(String userId, String applicationId, int score);

    /**
     * Provide the score list of a specific application in descending order
     *
     * @param applicationId application identifier
     * @param offset        offset of list
     * @param size          size of list
     * @return list of {@link RankedScore} in descending order
     */
    List<RankedScore> get(String applicationId, long offset, long size);

    /**
     * Provide score of a specific user in a specific application and nearby scores
     *
     * @param userId        user identifier
     * @param applicationId application identifier
     * @param top           number of top scores compare to the requested user score
     * @param bottom        number of bottom scores compare to the requested user score
     * @return list of {@link RankedScore} which contains user score and nearby ones and <code>null</code>
     * if there is not any score fot the user in the application
     */
    List<RankedScore> get(String userId, String applicationId, int top, int bottom);

    /**
     * Provide the score of a specific user for a specific application
     *
     * @param userId        user identifier
     * @param applicationId application identifier
     * @return a {@link RankedScore} instance which represent user score and current rank in rank list and <code>null</code>
     * if there is not any score fot the user in the application
     */
    RankedScore get(String userId, String applicationId);

    /**
     * Clear whole repository
     */
    void clear();

}
