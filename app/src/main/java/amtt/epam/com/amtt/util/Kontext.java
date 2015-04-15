package amtt.epam.com.amtt.util;

import android.content.Context;
import android.content.MutableContextWrapper;

/**
 * Created by Ivan_Bakach on 15.04.2015.
 */
public class Kontext extends MutableContextWrapper{

    private static Context kontext;

    public Kontext(Context base) {
        super(base);
        kontext = base;
    }

    public static void setKontext(Context kontext){
        Kontext.kontext = kontext;
    }

    public static Context getKontext(){
        return Kontext.kontext;
    }
}
