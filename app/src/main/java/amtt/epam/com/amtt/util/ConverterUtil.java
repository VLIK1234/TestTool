package amtt.epam.com.amtt.util;

import java.util.ArrayList;

/**
 * @author Iryna Monchenko
 * @version on 29.07.2015
 */

public class ConverterUtil {

    public static String[] arrayListToArray(ArrayList<String> arrayList){
        String[] array = new String[arrayList.size()];
        array = arrayList.toArray(array);
        return array;
    }
}
