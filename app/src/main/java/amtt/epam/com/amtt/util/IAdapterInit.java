package amtt.epam.com.amtt.util;

import java.util.List;

import amtt.epam.com.amtt.database.object.DatabaseEntity;

/**
 * Created by Ivan_Bakach on 12.06.2015.
 */
public interface IAdapterInit {
    void init(List<DatabaseEntity> result);
}
