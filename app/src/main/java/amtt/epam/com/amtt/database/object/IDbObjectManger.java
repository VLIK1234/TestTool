package amtt.epam.com.amtt.database.object;

import java.util.List;

/**
 @author Artsiom_Kaliaha
 @version on 11.05.2015
 */

public interface IDbObjectManger<ObjectType> {

    Integer add(ObjectType object);

    Integer update(ObjectType object, String selection, String[] selectionArgs);

    void remove(ObjectType object);

    void removeAll(ObjectType objectPrototype);

    void getAll(ObjectType object, IResult<List<ObjectType>> result);
}
