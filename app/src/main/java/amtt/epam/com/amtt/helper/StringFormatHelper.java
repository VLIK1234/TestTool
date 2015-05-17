package amtt.epam.com.amtt.helper;

/**
 * Created by Ivan_Bakach on 14.05.2015.
 */
public class StringFormatHelper {
    public static String format(String key, Object value) {
        if (value instanceof String[]) {
            String out = "\n" + key + ": ";
            for (int i =0; i<((String[]) value).length;i++) {
                out+=((String[]) value)[i]+" ";
            }
            return out;
        }
        return String.format("\n" + key + ": " + value);
    }
}
