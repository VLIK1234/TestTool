package amtt.epam.com.amtt.bo;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class IssueType extends Entity{

    private String id;
    private String name;

    public IssueType(){}

    public IssueType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
