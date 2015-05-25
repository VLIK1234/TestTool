package amtt.epam.com.amtt.database.object;

import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 11.05.2015.
 */
public interface IDbObjectManger<ObjectType> {

    Integer addOrUpdate(ObjectType object);

    Integer update(ObjectType objectPrototype);

    void remove(ObjectType object);

    void removeAll(ObjectType objectPrototype);

    List<ObjectType> getAll(ObjectType object);

    ObjectType getByKey(ObjectType objectPrototype);

}
