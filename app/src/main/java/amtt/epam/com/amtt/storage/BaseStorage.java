package amtt.epam.com.amtt.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ivan_Bakach on 26.03.2015.
 */
public class BaseStorage {
    //this storage for variable
    public static final String SCREEN_NUMBER = "Screen number";
    public static final String NUMBER = "Number";

    //TODO - what for do we need this fields?
    public static SharedPreferences setting;
    public static SharedPreferences.Editor editor;

    //TODO - what for do we need this class?
    private BaseStorage() {

    }

    public static void initialize(Context context) {
        setting = context.getSharedPreferences(SCREEN_NUMBER, Context.MODE_PRIVATE);
    }

    public static void setNumber(int number) {
        editor = setting.edit();
        editor.putInt(NUMBER, number);
        editor.apply();
    }

    public static int getNumber() {
        return setting.getInt(NUMBER, 0);
    }
}
