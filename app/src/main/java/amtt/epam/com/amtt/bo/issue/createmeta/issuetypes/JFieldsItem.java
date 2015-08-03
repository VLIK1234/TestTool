package amtt.epam.com.amtt.bo.issue.createmeta.issuetypes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 @author Iryna Monchanka
 @version on 3/31/2015
 */

public abstract class JFieldsItem {

    @SerializedName("required")
    private Boolean mRequired;
    @SerializedName("schema")
    private JFieldsItemSchema mSchema;
    @SerializedName("hasDefaultValue")
    private Boolean mHasDefaultValue;
    @SerializedName("name")
    private String mName;
    @SerializedName("operations")
    private List<String> mOperations;
    @SerializedName("allowedValues")
    private JFieldsItemAllowedValues mAllowedValues;

    protected JFieldsItem() {
    }

    protected JFieldsItem(Boolean required, JFieldsItemSchema schema, Boolean hasDefaultValue, String name, List<String> operations, JFieldsItemAllowedValues allowedValues) {
        this.mRequired = required;
        this.mSchema = schema;
        this.mHasDefaultValue = hasDefaultValue;
        this.mName = name;
        this.mOperations = operations;
        this.mAllowedValues = allowedValues;
    }

    public Boolean getRequired() {
        return mRequired;
    }

    public void setRequired(Boolean required) {
        this.mRequired = required;
    }

    public JFieldsItemSchema getSchema() {
        return mSchema;
    }

    public void setSchema(JFieldsItemSchema schema) {
        this.mSchema = schema;
    }

    public Boolean getHasDefaultValue() {
        return mHasDefaultValue;
    }

    public void setHasDefaultValue(Boolean hasDefaultValue) {
        this.mHasDefaultValue = hasDefaultValue;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public List<String> getOperations() {
        return mOperations;
    }

    public void setOperations(List<String> operations) {
        this.mOperations = operations;
    }

    public JFieldsItemAllowedValues getAllowedValues() {
        return mAllowedValues;
    }

    public void setAllowedValues(JFieldsItemAllowedValues allowedValues) {
        this.mAllowedValues = allowedValues;
    }
}
