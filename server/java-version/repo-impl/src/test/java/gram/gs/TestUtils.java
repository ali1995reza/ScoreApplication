package gram.gs;

import java.util.UUID;

public class TestUtils {


    public static String newId() {
        return getId(UUID.randomUUID());
    }

    public static String getId(Object o) {
        return o.toString();
    }

}
