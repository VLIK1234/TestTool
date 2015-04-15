package amtt.epam.com.amtt.util;

/**
 * Created by shiza on 09.04.2015.
 */
public class IsInputCorrect {//todo sounds like a name of method not a class

    //todo we discuss about creating methods like isUrlValid, isEmailValid etc.
    //this method and class as well could be swapped with TextUtils.isEmpty()
    //please make proper changes or remove class if it has no value
    public static Boolean isEmpty(String data) {
        if (data == null || data == "") {
            return true;
        } else {
            return false;
        }
    }


}
