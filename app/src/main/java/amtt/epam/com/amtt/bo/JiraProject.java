package amtt.epam.com.amtt.bo;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraProject extends Entity {

    private String id;
    private String key;

    public JiraProject() {
    }

    public JiraProject(String id, String key) {
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
