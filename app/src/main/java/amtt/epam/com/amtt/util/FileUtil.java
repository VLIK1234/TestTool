package amtt.epam.com.amtt.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ivan_Bakach on 10.06.2015.
 */
public class FileUtil {

    public static String getFileName(String filePath){
        Pattern p = Pattern.compile("[-_0-9a-zA-Z]*[.]\\w{0,5}");
        Matcher m = p.matcher(filePath);
        if (m.find()) {
            return m.group();
        }else{
            return "";
        }
    }
}
