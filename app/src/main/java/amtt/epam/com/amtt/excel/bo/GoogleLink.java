package amtt.epam.com.amtt.excel.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */

public class GoogleLink {

    private String mType;
    private String mHref;
    private String mRel;

    public GoogleLink() {
    }

    public GoogleLink(String type, String href, String rel) {
        this.mType = type;
        this.mHref = href;
        this.mRel = rel;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
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
