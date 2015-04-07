package amtt.epam.com.amtt.bo.issue;

import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Iryna_Monchanka on 06.04.2015.
 */
public enum TypeSearchedData {

    SEARCH_ISSUE("SearchIssue"),
    SEARCH_USER_INFO("SearchUserInfo");

    private static final String TAG = TypeSearchedData.class.getSimpleName();
    private String typeValue;

    private TypeSearchedData(String type) {
        typeValue = type;
    }

    static public TypeSearchedData getType(String pType) {
        for (TypeSearchedData type : TypeSearchedData.values()) {
            if (type.getTypeValue().equals(pType)) {
                return type;
            }
        }
        new Logger(TAG, "unknown type");
        return null;
    }

    public String getTypeValue() {
        return typeValue;
    }

}

