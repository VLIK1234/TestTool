package amtt.epam.com.amtt.googleapi.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */

public class GLink {

    private String mHref;
    private String mRel;

    public GLink() {
    }

    public GLink(String href, String rel) {
        this.mHref = href;
        this.mRel = rel;
    }

    public String getHref() {
        return mHref;
    }

    public void setHref(String href) {
        this.mHref = href;
    }

    public String getRel() {
        return mRel;
    }

    public void setRel(String rel) {
        this.mRel = rel;
    }
}
