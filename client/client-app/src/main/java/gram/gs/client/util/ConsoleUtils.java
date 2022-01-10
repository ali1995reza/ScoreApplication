package gram.gs.client.util;

import java.io.IOException;

public class ConsoleUtils {

    private final static String CLS;

    static {
        String s = "\r\n";
        for (int i = 0; i < 1000; i++) {
            s = s + "\r\n";
        }
        CLS = s;
    }

    public static void clear() {
        try {
            Runtime.getRuntime().exec("cls");
        } catch (IOException e) {
        }
        try {
            Runtime.getRuntime().exec("clear");
        } catch (IOException e) {
        }
    }
}
