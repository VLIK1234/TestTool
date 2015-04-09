package amtt.epam.com.amtt.util;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Objects;

/**
 * Created by shiza on 09.04.2015.
 */
public class InputsChecker {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Boolean isVoid(String data) {
        if ((Objects.equals(data, null)) || (Objects.equals(data, ""))) {
            return true;
        } else {
            return false;
        }
    }


}
