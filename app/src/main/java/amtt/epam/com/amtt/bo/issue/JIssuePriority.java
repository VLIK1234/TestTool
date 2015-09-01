package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.JBase;

/**
 @author Iryna Monchanka
 @version on 30.03.2015
 */

public class JIssuePriority extends JBase{

    @SerializedName("iconUrl")
    private String mIconUrl;

    public JIssuePriority(){}

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
    }

}
