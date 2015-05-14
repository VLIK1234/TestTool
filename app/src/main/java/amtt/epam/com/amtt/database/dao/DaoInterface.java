package amtt.epam.com.amtt.database.dao;

import android.content.Context;

import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 11.05.2015.
 */
public interface DaoInterface<ObjectType> {

    int add(ObjectType object) throws Exception;

    void remove(ObjectType object);

    void removeByKey(int key);

    void removeAll();

    void update(ObjectType object);

    List<ObjectType> getAll();

    ObjectType getByKey(int key);

}
