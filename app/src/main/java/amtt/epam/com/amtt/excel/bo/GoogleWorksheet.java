package amtt.epam.com.amtt.excel.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GoogleWorksheet extends GoogleSheet {

    private List<GoogleEntryWorksheet> mEntry;

    public GoogleWorksheet() {
    }

    public GoogleWorksheet(List<GoogleEntryWorksheet> entry) {
        this.mEntry = entry;
    }

    public GoogleWorksheet(String idLink, String updated, String title, GoogleLink selfLink,
                           GoogleLink alternateLink, GoogleLink feedLink, GoogleLink postLink,
                           GoogleAuthor author, int openSearchTotalResults, int openSearchStartIndex,
                           List<GoogleEntryWorksheet> entry) {
        super(idLink, updated, title, selfLink, alternateLink, feedLink, postLink, author,
                openSearchTotalResults, openSearchStartIndex);
        this.mEntry = entry;
    }

    public List<GoogleEntryWorksheet> getEntry() {
        return mEntry;
    }

    public void setEntry(List<GoogleEntryWorksheet> entry) {
        this.mEntry = entry;
    }

    public void setEntryItem(GoogleEntryWorksheet googleEntryWorksheet){
        if(mEntry==null){
            mEntry = new ArrayList<>();
        }
        mEntry.add(googleEntryWorksheet);
    }
}
