package gram.gs.util;

import java.util.regex.Pattern;

public class Utils {

    private final static Pattern ID_PATTERN = Pattern.compile("^([0-9a-zA-Z_\\-]{3,})+$");

    public static boolean isValidUserId(String userId) {
        if (userId == null) {
            return false;
        }
        return ID_PATTERN.matcher(userId).matches();
    }

    public static boolean isValidApplicationId(String applicationId) {
        if (applicationId == null) {
            return false;
        }
        return ID_PATTERN.matcher(applicationId).matches();
    }

}
