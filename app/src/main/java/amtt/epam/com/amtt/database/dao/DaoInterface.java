package amtt.epam.com.amtt.database.dao;

import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 11.05.2015.
 */
public interface DaoInterface<ObjectType> {

    Integer addOrUpdate(ObjectType object);

    Integer update(ObjectType objectPrototype);

    void remove(ObjectType object);

    void removeAll(ObjectType objectPrototype);

    List<ObjectType> getAll();

    ObjectType getByKey(ObjectType objectPrototype);

}
