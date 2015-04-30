package amtt.epam.com.amtt.processing;

import com.google.gson.GsonBuilder;

/**
 * Created by Artsiom_Kaliaha on 24.04.2015.
 */
public class Gson {

    private static class GsonSingletonHolder {
        public static final com.google.gson.Gson INSTANCE = new GsonBuilder().create();
    }

    private Gson() {}

    public static com.google.gson.Gson getInstance() {
        return GsonSingletonHolder.INSTANCE;
    }

}
