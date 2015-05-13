package amtt.epam.com.amtt.database.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artsiom_Kaliaha on 13.05.2015.
 */
public class DaoFactory {

    private static final Map<Class, DaoInterface> sDaoObjects;

    static {
        sDaoObjects = new HashMap<Class, DaoInterface>() {{
            put(StepDao.class, new StepDao());
            put(UserDao.class, new UserDao());
        }};
    }

    public static DaoInterface getDao(Class className) {
        return sDaoObjects.get(className);
    }

}
