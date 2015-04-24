package amtt.epam.com.amtt.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Iryna_Monchanka on 4/3/2015.
 */
public class Converter {

    public static ArrayList<String> setToArrayList(Set<String> inputSetString) {
        if (inputSetString != null) {
            ArrayList<String> outputArrayList = new ArrayList<>();
            for (String name : inputSetString) {
                outputArrayList.add(name);
            }
            return outputArrayList;
        } else {
            return null;
        }
    }

    public static Set<String> arrayListToSet(ArrayList<String> inputArrayList) {
        if (inputArrayList != null) {
            Set<String> outputSetString = new HashSet<>();
            for (int i = 0; i < inputArrayList.size(); i++) {
                outputSetString.add(inputArrayList.get(i));
            }
            return outputSetString;
        } else {
            return null;
        }
    }

}
