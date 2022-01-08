package gram.gs.util;

import java.util.regex.Pattern;

public class Utils {

    private final static Pattern USER_ID_PATTERN = Pattern.compile("^([a-zA-Z])+([\\w]{5,})+$");

    public static boolean isValidUserId(String userId) {
        if (userId == null) {
            return false;
        }
        return USER_ID_PATTERN.matcher(userId).matches();
    }

}
