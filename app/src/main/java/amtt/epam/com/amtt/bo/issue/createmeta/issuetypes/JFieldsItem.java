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
    private JFISchema mSchema;
    @SerializedName("hasDefaultValue")
    private Boolean mHasDefaultValue;
    @SerializedName("name")
    private String mName;
    @SerializedName("operations")
    private List<String> mOperations;
    @SerializedName("allowedValues")
    private JFIAllowedValues mAllowedValues;

    protected JFieldsItem() {
    }

    public Boolean getRequired() {
        return mRequired;
    }

    public void setRequired(Boolean required) {
        this.mRequired = required;
    }

    public JFISchema getSchema() {
        return mSchema;
    }

    public void setSchema(JFISchema schema) {
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

    public JFIAllowedValues getAllowedValues() {
        return mAllowedValues;
    }

    public void setAllowedValues(JFIAllowedValues allowedValues) {
        this.mAllowedValues = allowedValues;
    }
}
