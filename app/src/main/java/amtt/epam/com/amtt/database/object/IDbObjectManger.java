package amtt.epam.com.amtt.database.object;

import java.util.List;

import amtt.epam.com.amtt.common.Callback;

/**
 @author Artsiom_Kaliaha
 @version on 11.05.2015
 */

public interface IDbObjectManger<ObjectType> {

    <Entity extends ObjectType> Integer add(Entity object);

    <Entity extends ObjectType> Integer update(Entity object, String selection, String[] selectionArgs);

    <Entity extends ObjectType> void remove(Entity object, Callback<Integer> result);

    <Entity extends ObjectType> void removeAll(Entity objectPrototype, Callback<Integer> result);

    <Entity extends ObjectType> void getAll(Entity object, Callback<List<Entity>> result);
}
