package amtt.epam.com.amtt.database.dao;

import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 11.05.2015.
 */
public interface DaoInterface<ObjectType> {

    int addOrUpdate(ObjectType object) throws Exception;

    void remove(ObjectType object);

    void removeAll(ObjectType object);

    List<ObjectType> getAll();

    ObjectType getByKey(ObjectType object);

}
