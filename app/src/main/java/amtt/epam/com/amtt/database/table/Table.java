package amtt.epam.com.amtt.database.table;

import java.util.List;
import java.util.Map;
import java.util.Set;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.util.MultiValueMap;

/**
 @author Artsiom_Kaliaha
 @version on 20.03.2015
 */

public abstract class Table implements android.provider.BaseColumns {

    protected abstract MultiValueMap<String, String> getColumnsMap();

    public abstract String getTableName();

    public String getCreateQuery() {
        StringBuilder createQuery = new StringBuilder();
        createQuery.append(BaseColumns.CREATE).append(getTableName()).append(" ( ");

        Set<Map.Entry<String, List<String>>> keyValuePairs = getColumnsMap().entrySet();

        for (Map.Entry<String, List<String>> pair : keyValuePairs) {
            List<String> columns = pair.getValue();
            for (String column : columns) {
                createQuery.append(column).append(" ").append(pair.getKey()).append(BaseColumns.DIVIDER);
            }
        }
        createQuery.deleteCharAt(createQuery.length() - 2).append(" )");
        return createQuery.toString();
    }

}
