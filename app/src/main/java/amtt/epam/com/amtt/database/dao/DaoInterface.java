package amtt.epam.com.amtt.database.dao;

import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 11.05.2015.
 */
public interface DaoInterface<ObjectType> {

    int add(ObjectType object) throws Exception;

    void remove(ObjectType object) throws Exception;

    void removeByKey(int key) throws Exception;

    void removeAll() throws Exception;

    void update(ObjectType object) throws Exception;

    List<ObjectType> getAll() throws Exception;

    ObjectType getByKey(int key) throws Exception;

}
