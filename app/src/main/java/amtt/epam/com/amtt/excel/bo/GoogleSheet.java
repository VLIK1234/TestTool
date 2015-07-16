package amtt.epam.com.amtt.excel.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GoogleSheet<T> extends GoogleBase<T> {

    protected GoogleLink mAlternateLink;
    protected GoogleLink mFeedLink;
    protected GoogleLink mPostLink;
    protected GoogleAuthor mAuthor;
    protected int mOpenSearchTotalResults;
    protected int mOpenSearchStartIndex;

    public GoogleSheet() {
    }

    public GoogleSheet(String idLink, String updated, String title, GoogleLink selfLink,
                       GoogleLink alternateLink, GoogleLink feedLink, GoogleLink postLink,
                       GoogleAuthor author, int openSearchTotalResults, int openSearchStartIndex) {
        super(idLink, updated, title, selfLink);
        this.mAlternateLink = alternateLink;
        this.mFeedLink = feedLink;
        this.mPostLink = postLink;
        this.mAuthor = author;
        this.mOpenSearchTotalResults = openSearchTotalResults;
        this.mOpenSearchStartIndex = openSearchStartIndex;
    }

    public GoogleLink getAlternateLink() {
        return mAlternateLink;
    }

    public void setAlternateLink(GoogleLink alternateLink) {
        this.mAlternateLink = alternateLink;
    }

    public GoogleLink getFeedLink() {
        return mFeedLink;
    }

    public void setFeedLink(GoogleLink feedLink) {
        this.mFeedLink = feedLink;
    }

    public GoogleLink getPostLink() {
        return mPostLink;
    }

    public void setPostLink(GoogleLink postLink) {
        this.mPostLink = postLink;
    }

    public GoogleAuthor getAuthor() {
        return mAuthor;
    }

    public void setAuthor(GoogleAuthor author) {
        this.mAuthor = author;
    }

    public int getOpenSearchTotalResults() {
        return mOpenSearchTotalResults;
    }

    public void setOpenSearchTotalResults(int openSearchTotalResults) {
        this.mOpenSearchTotalResults = openSearchTotalResults;
    }

    public int getOpenSearchStartIndex() {
        return mOpenSearchStartIndex;
    }

    public void setOpenSearchStartIndex(int openSearchStartIndex) {
        this.mOpenSearchStartIndex = openSearchStartIndex;
    }
}
