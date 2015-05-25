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

    void getAll(ObjectType object, IResult<List<ObjectType>> result);

    void getByKey(ObjectType objectPrototype, IResult<List<ObjectType>> result);

}
