package amtt.epam.com.amtt.database.dao;

import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 11.05.2015.
 */
public interface DaoInterface<ObjectType> {

    int add(ObjectType object);

    void remove(ObjectType object);

    void update(ObjectType object);

    List<ObjectType> getAll();

    ObjectType getByKey(int key);

    void removeByKey(int key);

}
