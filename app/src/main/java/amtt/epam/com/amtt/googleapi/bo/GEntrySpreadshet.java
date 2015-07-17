package amtt.epam.com.amtt.googleapi.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GEntrySpreadshet extends GEntry {

    private GLink mListFeedLink;
    private GLink mCellsFeedLink;
    private GLink mVisualisationApiLink;
    private GLink mExportCSVLink;
    private int mGSColCount;
    private int mGSRowCount;

    public GEntrySpreadshet() {
    }

    public GEntrySpreadshet(String idLink, String updated, String title, String content, GLink listFeedLink,
                            GLink cellsFeedLink, GLink visualisationApiLink, GLink exportCSVLink,
                            GLink selfLink, int gsColCount, int gsRowCount) {
        super(idLink, updated, title, selfLink, content);
        this.mListFeedLink = listFeedLink;
        this.mCellsFeedLink = cellsFeedLink;
        this.mVisualisationApiLink = visualisationApiLink;
        this.mExportCSVLink = exportCSVLink;
        this.mGSColCount = gsColCount;
        this.mGSRowCount = gsRowCount;
    }

    public GLink getListFeedLink() {
        return mListFeedLink;
    }

    public void setListFeedLink(GLink listFeedLink) {
        this.mListFeedLink = listFeedLink;
    }

    public GLink getCellsFeedLink() {
        return mCellsFeedLink;
    }

    public void setCellsFeedLink(GLink cellsFeedLink) {
        this.mCellsFeedLink = cellsFeedLink;
    }

    public GLink getVisualisationApiLink() {
        return mVisualisationApiLink;
    }

    public void setVisualisationApiLink(GLink visualisationApiLink) {
        this.mVisualisationApiLink = visualisationApiLink;
    }

    public GLink getExportCSVLink() {
        return mExportCSVLink;
    }

    public void setExportCSVLink(GLink exportCSVLink) {
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
