package gram.gs.server;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

public class InputUtils {

    public static Integer getInteger(String msg, String msgTryAgain, Function<Integer, Boolean> validator, Scanner input) {
        System.out.print(msg + " : ");
        while (true) {
            String line = input.nextLine().trim();
            try {
                Integer i = Integer.parseInt(line);
                if (!validator.apply(i)) {
                    throw new IllegalStateException();
                }
                return i;
            } catch (Exception e) {
                System.out.print(msgTryAgain + " : ");
            }
        }
    }

    public static String getString(String msg, String msgTryAgain, Function<String, Boolean> validator, Scanner input) {
        System.out.print(msg + " : ");
        while (true) {
            String line = input.nextLine().trim();
            try {
                if (!validator.apply(line)) {
                    throw new IllegalStateException();
                }
                return line;
            } catch (Exception e) {
                System.out.print(msgTryAgain + " : ");
            }
        }
    }

    public static String getValidString(String msg, List<String> valid, Scanner input) {
        Set<String> set = new HashSet<>(valid);
        return getString(msg, "please enter a valid value " + valid, s -> set.contains(s), input);
    }

}
