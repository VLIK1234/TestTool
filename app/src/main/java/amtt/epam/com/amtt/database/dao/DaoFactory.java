package amtt.epam.com.amtt.database.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artsiom_Kaliaha on 13.05.2015.
 */
public class DaoFactory {

    private static final Map<String, DaoInterface> sDaoObjects;

    static {
        sDaoObjects = new HashMap<String, DaoInterface>() {{
            put(StepDao.TAG, new StepDao());
            put(UserDao.TAG, new UserDao());
        }};
    }

    public static DaoInterface getDao(String className) {
        return sDaoObjects.get(className);
    }

}
