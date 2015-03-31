package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.bo.issue.createmeta.JITFieldsItem;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraIssueTypesIssueType extends JITFieldsItem {

    private String id;
    private String name;
    private String self;
    private String description;
    private String iconUrl;
    private Boolean subtask;


    public JiraIssueTypesIssueType() {
    }

    public JiraIssueTypesIssueType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean getSubtask() {
        return subtask;
    }

    public void setSubtask(Boolean subtask) {
        this.subtask = subtask;
    }
}
