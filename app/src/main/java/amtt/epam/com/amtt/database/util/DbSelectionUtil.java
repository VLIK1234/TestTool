package amtt.epam.com.amtt.database.util;

/**
 * @author Iryna Monchenko
 * @version on 21.08.2015
 */

public class DbSelectionUtil {

    public static String equal(String column) {
        return column + "=?";
    }

    public static String and(String first, String second) {
        return first + " AND " + second;
    }

}
