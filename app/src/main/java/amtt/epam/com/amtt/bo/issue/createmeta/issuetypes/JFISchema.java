package amtt.epam.com.amtt.bo.issue.createmeta.issuetypes;

import com.google.gson.annotations.SerializedName;

/**
 @author Iryna Monchanka
 @version on 3/31/2015
 */

public class JFISchema {

    @SerializedName("items")
    private String mItems;
    @SerializedName("system")
    private String mSystem;
    @SerializedName("type")
    private String mType;

    public JFISchema() {

    }

    public JFISchema(String items, String system, String type) {

        this.mItems = items;
        this.mSystem = system;
        this.mType = type;
    }

    public String getItems() {
        return mItems;
    }

    public void setItems(String mItems) {
        this.mItems = mItems;
    }

    public String getSystem() {
        return mSystem;
    }

    public void setSystem(String mSystem) {
        this.mSystem = mSystem;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
