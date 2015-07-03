package amtt.epam.com.amtt.excel.bo;

/**
 * @author Iryna Monchanka
 * @version on 03.07.2015
 */
public final class GoogleApiConst {

    private static final String BASE_PATH = "https://spreadsheets.google.com/feeds/";
    private static final String WORKSHEETS = "worksheets/";
    private static final String LIST = "list/";
    private static final String DEFAULT_SPREADSHEET_KEY = "1t9Yl39HHl4FjXkT9vGu__Q0glvdIk6SctGrVjeACt5k/";
    public static final String PATH_POSTFIX = "public/full";
    public static final String SPREADSHEET_PATH = BASE_PATH + WORKSHEETS + DEFAULT_SPREADSHEET_KEY + PATH_POSTFIX;
    public static final String WORKSHEET_PATH = BASE_PATH + LIST;

    public static final String ID_TAG = "id";
    public static final String UPDATED_TAG = "updated";
    public static final String TITLE_TAG = "title";
    public static final String LINK_TAG = "link";
    public static final String AUTHOR_TAG = "author";
    public static final String NAME_TAG = "name";
    public static final String EMAIL_TAG = "email";
    public static final String TOTAL_RESULTS_TAG = "openSearch:totalResults";
    public static final String START_INDEX_TAG = "openSearch:startIndex";
    public static final String ENTRY_TAG = "entry";
    public static final String CONTENT_TAG = "content";
    public static final String COL_COUNT_TAG = "gs:colCount";
    public static final String ROW_COUNT_TAG = "gs:rowCount";
    public static final String REL_ATTR = "rel";
    public static final String ALTERNATE_ATTR = "alternate";
    public static final String FEED_ATTR = "http://schemas.google.com/g/2005#feed";
    public static final String POST_ATTR = "http://schemas.google.com/g/2005#post";
    public static final String SELF_ATTR = "self";
    public static final String LISTFEED_ATTR = "http://schemas.google.com/spreadsheets/2006#listfeed";
    public static final String CELLSFEED_ATTR = "http://schemas.google.com/spreadsheets/2006#cellsfeed";
    public static final String VISUALISATION_API_ATTR = "http://schemas.google.com/visualization/2008#visualizationApi";
    public static final String EXPORTCSV_ATTR = "http://schemas.google.com/spreadsheets/2006#exportcsv";

}
