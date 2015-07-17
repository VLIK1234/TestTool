package amtt.epam.com.amtt.googleapi.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GAuthor {

    private String mName;
    private String mEmail;

    public GAuthor() {
    }

    public GAuthor(String name, String email) {
        this.mName = name;
        this.mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
