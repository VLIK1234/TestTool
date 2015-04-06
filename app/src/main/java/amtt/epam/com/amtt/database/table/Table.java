package amtt.epam.com.amtt.database.table;

import amtt.epam.com.amtt.database.BaseColumns;
import amtt.epam.com.amtt.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Artsiom_Kaliaha on 20.03.2015.
 */
public abstract class Table implements BaseColumns {

    public abstract MultiValueMap<String, String> getColumnsMap();

    public abstract String getTableName();

    public String getCreateQuery() {
        StringBuilder createQuery = new StringBuilder();
        createQuery.append(BaseColumns.CREATE).append(getTableName()).append(" ( ");

        Set<Map.Entry<String, List<String>>> keyValuePairs = getColumnsMap().entrySet();

        for (Map.Entry<String, List<String>> pair : keyValuePairs) {
            List<String> columns = pair.getValue();
            for (String column : columns) {
                createQuery.append(column).append(" ").append(pair.getKey()).append(DIVIDER);
            }
        }
        createQuery.deleteCharAt(createQuery.length() - 2).append(" )");
        return createQuery.toString();
    }

}
