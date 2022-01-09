package gram.gs.client.util;

import java.util.function.Supplier;

public class NumericParser {

    public static <T extends Throwable> Long parseLong(String longStr, Long def, Supplier<T> ex) throws T {
        if (longStr == null) {
            return def;
        }
        try {
            return Long.parseLong(longStr);
        } catch (Exception e) {
            throw ex.get();
        }
    }

    public static <T extends Throwable> Long parseLong(String longStr, Long def, String name) throws T {
        return parseLong(longStr, def, () -> new NumberFormatException(name + " is not a valid number"));
    }

    public static <T extends Throwable> Long parseLong(String longStr, Supplier<T> ex) throws T {
        if (longStr == null) {
            throw ex.get();
        }
        try {
            return Long.parseLong(longStr);
        } catch (Exception e) {
            throw ex.get();
        }
    }

    public static <T extends Throwable> Long parseLong(String longStr, String name) throws T {
        return parseLong(longStr, () -> new NumberFormatException(name + " is not a valid number"));
    }

    public static <T extends Throwable> Integer parseInteger(String intStr, Integer def, Supplier<T> ex) throws T {
        if (intStr == null) {
            return def;
        }
        try {
            return Integer.parseInt(intStr);
        } catch (Exception e) {
            throw ex.get();
        }
    }

    public static <T extends Throwable> Integer parseInteger(String intStr, Integer def, String name) throws T {
        return parseInteger(intStr, def, () -> new NumberFormatException(name + " is not a valid number"));
    }

    public static <T extends Throwable> Integer parseInteger(String intStr, Supplier<T> ex) throws T {
        if (intStr == null) {
            throw ex.get();
        }
        try {
            return Integer.parseInt(intStr);
        } catch (Exception e) {
            throw ex.get();
        }
    }

    public static <T extends Throwable> Integer parseInteger(String intStr, String name) throws T {
        return parseInteger(intStr, () -> new NumberFormatException(name + " is not a valid number"));
    }
}
