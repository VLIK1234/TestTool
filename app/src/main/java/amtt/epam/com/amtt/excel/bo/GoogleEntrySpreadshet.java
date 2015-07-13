package amtt.epam.com.amtt.excel.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GoogleEntrySpreadshet extends GoogleEntry{

    private GoogleLink mListFeedLink;
    private GoogleLink mCellsFeedLink;
    private GoogleLink mVisualisationApiLink;
    private GoogleLink mExportCSVLink;
    private int mGSColCount;
    private int mGSRowCount;

    public GoogleEntrySpreadshet() {
    }

    public GoogleEntrySpreadshet(String idLink, String updated, String title, String content, GoogleLink listFeedLink,
                       GoogleLink cellsFeedLink, GoogleLink visualisationApiLink, GoogleLink exportCSVLink,
                       GoogleLink selfLink, int gsColCount, int gsRowCount) {
        super(idLink, updated, title, selfLink, content);
        this.mListFeedLink = listFeedLink;
        this.mCellsFeedLink = cellsFeedLink;
        this.mVisualisationApiLink = visualisationApiLink;
        this.mExportCSVLink = exportCSVLink;
        this.mGSColCount = gsColCount;
        this.mGSRowCount = gsRowCount;
    }

    public GoogleLink getListFeedLink() {
        return mListFeedLink;
    }

    public void setListFeedLink(GoogleLink listFeedLink) {
        this.mListFeedLink = listFeedLink;
    }

    public GoogleLink getCellsFeedLink() {
        return mCellsFeedLink;
    }

    public void setCellsFeedLink(GoogleLink cellsFeedLink) {
        this.mCellsFeedLink = cellsFeedLink;
    }

    public GoogleLink getVisualisationApiLink() {
        return mVisualisationApiLink;
    }

    public void setVisualisationApiLink(GoogleLink visualisationApiLink) {
        this.mVisualisationApiLink = visualisationApiLink;
    }

    public GoogleLink getExportCSVLink() {
        return mExportCSVLink;
    }

    public void setExportCSVLink(GoogleLink exportCSVLink) {
        this.mExportCSVLink = exportCSVLink;
    }

    public int getGSColCount() {
        return mGSColCount;
    }

    public void setGSColCount(int gsColCount) {
        this.mGSColCount = gsColCount;
    }

    public int getGSRowCount() {
        return mGSRowCount;
    }

    public void setGSRowCount(int gsRowCount) {
        this.mGSRowCount = gsRowCount;
    }
}
