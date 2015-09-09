package amtt.epam.com.amtt.bo;

import com.google.gson.annotations.SerializedName;

/**
 * @author Iryna Monchanka
 * @version on 9/1/2015
 */
public class JBaseType extends JBase {

    @SerializedName("description")
    protected String mDescription;
    @SerializedName("iconUrl")
    protected String mIconUrl;
    @SerializedName("subtask")
    protected Boolean mSubtask;

    public JBaseType(){}

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public Boolean getSubtask() {
        return mSubtask;
    }

    public void setSubtask(Boolean subtask) {
        mSubtask = subtask;
    }
}
