package amtt.epam.com.amtt.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ivan_Bakach on 26.03.2015.
 */
public class ScreenNumber {
    public static final String SCREEN_NUMBER = "Screen number";
    public static final String NUMBER = "Number";
    public static SharedPreferences setting;
    public static SharedPreferences.Editor editor;

    private ScreenNumber(){

    }

    public static void initialize(Context context) {
        setting = context.getSharedPreferences(SCREEN_NUMBER, Context.MODE_PRIVATE);
        editor = setting.edit();
        editor.apply();
    }

    public static void setNumber(int number) {
        editor.putInt(NUMBER, number);
        editor.apply();
    }

    public static int getNumber() {
        return setting.getInt(NUMBER, 0);
    }
}
