package amtt.epam.com.amtt.bo.issue.issuekey;

import amtt.epam.com.amtt.bo.JiraAvatarUrls;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraIssueProject {

    private String id;
    private String key;
    private String self;
    private String name;
    private JiraAvatarUrls avatarUrls;


    public JiraIssueProject() {
    }

    public JiraIssueProject(String id, String key) {
        this.id = id;
        this.key = key;
    }

    public String getIdProject() {
        return id;
    }

    public void setIdProject(String id) {
        this.id = id;
    }

    public String getKeyProject() {
        return key;
    }

    public void setKeyProject(String key) {
        this.key = key;
    }
}
