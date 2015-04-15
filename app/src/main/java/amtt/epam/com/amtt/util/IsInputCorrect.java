package amtt.epam.com.amtt.util;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Objects;

/**
 * Created by shiza on 09.04.2015.
 */
public class IsInputCorrect {

    public static Boolean isEmpty(String data) {
        if (data == null || data == "") {
            return true;
        } else {
            return false;
        }
    }


}
