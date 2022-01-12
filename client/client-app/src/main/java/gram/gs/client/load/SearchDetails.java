package gram.gs.client.load;

final class SearchDetails {

    private final String userId;
    private final String applicationId;

    public SearchDetails(String userId, String applicationId) {
        this.userId = userId;
        this.applicationId = applicationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getApplicationId() {
        return applicationId;
    }

}
