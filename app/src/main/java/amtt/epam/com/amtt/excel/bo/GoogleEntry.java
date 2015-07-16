package amtt.epam.com.amtt.excel.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GoogleEntry<T> extends GoogleBase<T> {

    protected String mContent;

    public GoogleEntry() {
    }

    public GoogleEntry(String idLink, String updated, String title, GoogleLink selfLink, String content) {
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
