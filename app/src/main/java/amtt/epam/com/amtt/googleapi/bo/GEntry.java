package amtt.epam.com.amtt.googleapi.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GEntry<T> extends GBase<T> {

    protected String mContent;

    public GEntry() {
    }

    public GEntry(String idLink, String updated, String title, GLink selfLink, String content) {
        super(idLink, updated, title, selfLink);
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

}
