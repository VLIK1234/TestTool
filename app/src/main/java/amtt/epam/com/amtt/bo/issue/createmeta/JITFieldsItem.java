package amtt.epam.com.amtt.bo.issue.createmeta;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public abstract class JITFieldsItem {

    @SerializedName("required")
    private Boolean mRequired;
    @SerializedName("schema")
    private JITFieldsItemSchema mSchema;
    @SerializedName("hasDefaultValue")
    private Boolean mHasDefaultValue;
    @SerializedName("name")
    private String mName;
    @SerializedName("operations")
    private String[] mOperations;
    @SerializedName("allowedValues")
    private JITFieldsItemAllowedValues mAllowedValues;

    protected JITFieldsItem() {
    }

    protected JITFieldsItem(Boolean required, JITFieldsItemSchema schema, Boolean hasDefaultValue, String name, String[] operations, JITFieldsItemAllowedValues allowedValues) {
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

    public JITFieldsItemSchema getSchema() {
        return mSchema;
    }

    public void setSchema(JITFieldsItemSchema schema) {
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

    public String[] getOperations() {
        return mOperations;
    }

    public void setOperations(String[] operations) {
        this.mOperations = operations;
    }

    public JITFieldsItemAllowedValues getAllowedValues() {
        return mAllowedValues;
    }

    public void setAllowedValues(JITFieldsItemAllowedValues allowedValues) {
        this.mAllowedValues = allowedValues;
    }
}
