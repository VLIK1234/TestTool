package amtt.epam.com.amtt.excel.api;

/**
 * @author Iryna Monchanka
 * @version on 07.07.2015
 */

public interface ContentLoadingCallback<Content> {

    void resultFromBackend(Content result, GoogleApiConst.ContentType tag,
                           GoogleSpreadsheetContentCallback spreadsheetContentCallback);

}
