package amtt.epam.com.amtt.excel.bo;

import java.util.List;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GoogleSpreadsheet extends GoogleSheet {

    private List<GoogleEntrySpreadshet> mEntry;

    public GoogleSpreadsheet() {
    }

    public GoogleSpreadsheet(List<GoogleEntrySpreadshet> mEntry) {
        this.mEntry = mEntry;
    }

    public GoogleSpreadsheet(String idLink, String updated, String title, GoogleLink selfLink,
                             GoogleLink alternateLink, GoogleLink feedLink, GoogleLink postLink,
                             GoogleAuthor author, int openSearchTotalResults, int openSearchStartIndex,
                             List<GoogleEntrySpreadshet> entry) {
        super(idLink, updated, title, selfLink, alternateLink, feedLink, postLink, author,
                openSearchTotalResults, openSearchStartIndex);
        this.mEntry = entry;
    }

    public List<GoogleEntrySpreadshet> getEntry() {
        return mEntry;
    }

    public void setEntry(List<GoogleEntrySpreadshet> entry) {
        this.mEntry = entry;
    }
}
