package amtt.epam.com.amtt.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Iryna_Monchanka on 4/3/2015.
 */
public class Converter {

    public static ArrayList<String> setToArrayList(Set<String> inputSetString) {
        //TODO check input data before create any objects
        ArrayList<String> outputArrayList = new ArrayList<>();
        if (inputSetString != null) {
            for (String name : inputSetString) {
                outputArrayList.add(name);
            }
        }
        return outputArrayList;
    }

    public static Set<String> arrayListToSet(ArrayList<String> inputArrayList) {
        //TODO check input data before create any objects
        Set<String> outputSetString = new HashSet<>();
        for (int i = 0; i < inputArrayList.size(); i++) {
            outputSetString.add(inputArrayList.get(i));
        }
        return outputSetString;
    }

}
