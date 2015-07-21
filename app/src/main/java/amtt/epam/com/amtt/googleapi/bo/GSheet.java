package amtt.epam.com.amtt.googleapi.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GSheet<T> extends GBase<T> {

    protected GLink mAlternateLink;
    protected GLink mFeedLink;
    protected GLink mPostLink;
    protected GAuthor mAuthor;
    protected int mOpenSearchTotalResults;
    protected int mOpenSearchStartIndex;

    public GSheet() {
    }

    public GSheet(String idLink, String updated, String title, GLink selfLink,
                  GLink alternateLink, GLink feedLink, GLink postLink,
                  GAuthor author, int openSearchTotalResults, int openSearchStartIndex) {
        super(idLink, updated, title, selfLink);
        this.mAlternateLink = alternateLink;
        this.mFeedLink = feedLink;
        this.mPostLink = postLink;
        this.mAuthor = author;
        this.mOpenSearchTotalResults = openSearchTotalResults;
        this.mOpenSearchStartIndex = openSearchStartIndex;
    }

    public GLink getAlternateLink() {
        return mAlternateLink;
    }

    public void setAlternateLink(GLink alternateLink) {
        this.mAlternateLink = alternateLink;
    }

    public GLink getFeedLink() {
        return mFeedLink;
    }

    public void setFeedLink(GLink feedLink) {
        this.mFeedLink = feedLink;
    }

    public GLink getPostLink() {
        return mPostLink;
    }

    public void setPostLink(GLink postLink) {
        this.mPostLink = postLink;
    }

    public GAuthor getAuthor() {
        return mAuthor;
    }

    public void setAuthor(GAuthor author) {
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
