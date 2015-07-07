package amtt.epam.com.amtt.excel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import amtt.epam.com.amtt.excel.bo.GoogleApiConst;
import amtt.epam.com.amtt.excel.bo.GoogleAuthor;
import amtt.epam.com.amtt.excel.bo.GoogleEntrySpreadshet;
import amtt.epam.com.amtt.excel.bo.GoogleEntryWorksheet;
import amtt.epam.com.amtt.excel.bo.GoogleLink;
import amtt.epam.com.amtt.excel.bo.GoogleSpreadsheet;
import amtt.epam.com.amtt.excel.bo.GoogleWorksheet;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 02.07.2015
 */

public class XMLParser {

    private static XmlPullParserFactory xmlFactoryObject;
    private static final String TAG = XMLParser.class.getSimpleName();
    private static XmlPullParser xmlPullParser;

    public static GoogleSpreadsheet spreadsheetParse(HttpURLConnection conn) {
        try {
            InputStream stream = conn.getInputStream();
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlFactoryObject.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(stream, null);
            GoogleSpreadsheet spreadshet = new GoogleSpreadsheet();
            xmlPullParser.nextTag();
            xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.FEED_TAG);
            Logger.d(TAG, "SPREADSHEET BEGIN");
            xmlPullParser.nextTag();
            while (xmlPullParser.getDepth() != 0) {
                spreadshet.setIdLink(loadIdLink());
                spreadshet.setUpdated(loadUpdated());
                skipTag();//category
                spreadshet.setTitle(loadTitle());
                spreadshet.setAlternateLink(loadGoogleLink());
                spreadshet.setFeedLink(loadGoogleLink());
                spreadshet.setPostLink(loadGoogleLink());
                spreadshet.setSelfLink(loadGoogleLink());
                spreadshet.setAuthor(loadAuthor());
                spreadshet.setOpenSearchTotalResults(loadTotalResults());
                spreadshet.setOpenSearchStartIndex(loadStartIndex());
                for (int i = 0; i < spreadshet.getOpenSearchTotalResults(); i++) {
                    GoogleEntrySpreadshet entrySpreadshet = new GoogleEntrySpreadshet();
                    Logger.d(TAG, "SPREADSHEET ENTRY BEGIN");
                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ENTRY_TAG);
                    xmlPullParser.nextTag();
                    entrySpreadshet.setIdLink(loadIdLink());
                    entrySpreadshet.setUpdated(loadUpdated());
                    skipTag();//category
                    entrySpreadshet.setTitle(loadTitle());
                    entrySpreadshet.setContent(loadContent());
                    entrySpreadshet.setListFeedLink(loadGoogleLink());
                    entrySpreadshet.setCellsFeedLink(loadGoogleLink());
                    entrySpreadshet.setVisualisationApiLink(loadGoogleLink());
                    entrySpreadshet.setExportCSVLink(loadGoogleLink());
                    entrySpreadshet.setSelfLink(loadGoogleLink());
                    entrySpreadshet.setGSColCount(loadColCount());
                    entrySpreadshet.setGSRowCount(loadRowCount());
                    spreadshet.setEntryItem(entrySpreadshet);
                    Logger.d(TAG, "SPREADSHEET ENTRY END");
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ENTRY_TAG);
                    xmlPullParser.nextTag();
                }
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.FEED_TAG);
                Logger.d(TAG, "SPREADSHEET END");
                xmlPullParser.next();
            }
            stream.close();
            return spreadshet;
        } catch (Throwable t) {
            Logger.e(TAG, "Error loading xml : " + t.toString(), t);
            return null;
        }
    }

    private static int loadRowCount() throws XmlPullParserException, IOException {
        return readInt(GoogleApiConst.ROW_COUNT_TAG);
    }

    private static int loadColCount() throws XmlPullParserException, IOException {
        return readInt(GoogleApiConst.COL_COUNT_TAG);
    }

    private static String loadContent() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.CONTENT_TAG);
    }

    private static int loadStartIndex() throws XmlPullParserException, IOException {
        return readInt(GoogleApiConst.START_INDEX_TAG);
    }

    private static int loadTotalResults() throws XmlPullParserException, IOException {
        return readInt(GoogleApiConst.TOTAL_RESULTS_TAG);
    }

    private static GoogleAuthor loadAuthor() throws XmlPullParserException, IOException {
        GoogleAuthor author = new GoogleAuthor();
        xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.AUTHOR_TAG);
        xmlPullParser.nextTag();
        author.setName(readString(GoogleApiConst.NAME_TAG));
        author.setEmail(readString(GoogleApiConst.EMAIL_TAG));
        xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.AUTHOR_TAG);
        xmlPullParser.nextTag();
        return author;
    }

    private static String loadTitle() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.TITLE_TAG);
    }

    private static String loadUpdated() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.UPDATED_TAG);
    }

    private static String loadIdLink() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.ID_TAG);
    }

    private static String loadIdGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.ID_GSX_TAG);
    }

    private static String loadPriorityGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.PRIORITY_GSX_TAG);
    }

    private static String loadTestCaseNameGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.TC_NAME_GSX_TAG);
    }

    private static String loadTestCaseDescriptionGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.TC_DESCRIPTION_GSX_TAG);
    }

    private static String loadTestStepsGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.TEST_STEPS_GSX_TAG);
    }

    private static String loadLabelGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.LABEL_GSX_TAG);
    }

    private static String loadExpectedResultsGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.EXPECTED_RESULT_GSX_TAG);
    }

    private static String readString(String tag) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, null, tag);
        String result = xmlPullParser.nextText();
        xmlPullParser.require(XmlPullParser.END_TAG, null, tag);
        xmlPullParser.nextTag();
        return result;
    }

    private static int readInt(String tag) throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, null, tag);
        int result = Integer.parseInt(xmlPullParser.nextText());
        xmlPullParser.require(XmlPullParser.END_TAG, null, tag);
        xmlPullParser.nextTag();
        return result;
    }

    private static void skipTag() throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
            Logger.e(TAG, "null tag");
        } else {
            int depth = 1;
            while (depth != 0) {
                switch (xmlPullParser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
            xmlPullParser.nextTag();
        }
    }

    private static GoogleLink loadGoogleLink() {
        GoogleLink googleLink = new GoogleLink();
        try {
            xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);

            String tag = xmlPullParser.getName();
            String relType = xmlPullParser.getAttributeValue(null, GoogleApiConst.REL_ATTR);
            if (tag.equals(GoogleApiConst.LINK_TAG)) {
                switch (relType) {
                    case GoogleApiConst.FEED_ATTR:
                        googleLink.setRel(relType);
                        googleLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        break;
                    case GoogleApiConst.POST_ATTR:
                        googleLink.setRel(relType);
                        googleLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        break;
                    case GoogleApiConst.SELF_ATTR:
                        googleLink.setRel(relType);
                        googleLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        break;
                    case GoogleApiConst.ALTERNATE_ATTR:
                        googleLink.setRel(relType);
                        googleLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        break;
                    case GoogleApiConst.LISTFEED_ATTR:
                        googleLink.setRel(relType);
                        googleLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        break;
                    case GoogleApiConst.CELLSFEED_ATTR:
                        googleLink.setRel(relType);
                        googleLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        break;
                    case GoogleApiConst.VISUALISATION_API_ATTR:
                        googleLink.setRel(relType);
                        googleLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        break;
                    case GoogleApiConst.EXPORTCSV_ATTR:
                        googleLink.setRel(relType);
                        googleLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        break;
                }
            }
            xmlPullParser.nextTag();
            xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.LINK_TAG);
            xmlPullParser.nextTag();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return googleLink;
    }

    public static synchronized void fetchXML() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(GoogleApiConst.SPREADSHEET_PATH);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    XMLContent.getInstance().setSpreadsheet(spreadsheetParse(conn));
                    conn.disconnect();
                    if (XMLContent.getInstance().getSpreadsheet() != null) {
                        URL url2 = new URL(XMLContent.getInstance().getSpreadsheet().getListWorksheets().get(3));
                        HttpURLConnection con = (HttpURLConnection) url2.openConnection();
                        con.setReadTimeout(10000 /* milliseconds */);
                        con.setConnectTimeout(15000 /* milliseconds */);
                        con.setRequestMethod("GET");
                        con.setDoInput(true);
                        con.connect();
                        XMLContent.getInstance().setWorksheet(worksheetParse(con));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static GoogleWorksheet worksheetParse(HttpURLConnection conn) {
        try {
            InputStream stream = conn.getInputStream();
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlFactoryObject.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(stream, null);

            GoogleWorksheet worksheet = new GoogleWorksheet();
            xmlPullParser.nextTag();
            xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.FEED_TAG);
            xmlPullParser.nextTag();
            Logger.d(TAG, "WORKSHEET BEGIN");
            while (xmlPullParser.getDepth() != 0) {
                worksheet.setIdLink(loadIdLink());
                worksheet.setUpdated(loadUpdated());
                skipTag();//category
                worksheet.setTitle(loadTitle());
                worksheet.setAlternateLink(loadGoogleLink());
                worksheet.setFeedLink(loadGoogleLink());
                worksheet.setPostLink(loadGoogleLink());
                worksheet.setSelfLink(loadGoogleLink());
                worksheet.setAuthor(loadAuthor());
                worksheet.setOpenSearchTotalResults(loadTotalResults());
                worksheet.setOpenSearchStartIndex(loadStartIndex());
                for (int i = 0; i < worksheet.getOpenSearchTotalResults(); i++) {
                    GoogleEntryWorksheet entryWorksheet = new GoogleEntryWorksheet();
                    Logger.d(TAG, "WORKSHEET ENTRY BEGIN");
                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ENTRY_TAG);
                    xmlPullParser.nextTag();
                    entryWorksheet.setIdLink(loadIdLink());
                    entryWorksheet.setUpdated(loadUpdated());
                    skipTag();
                    entryWorksheet.setTitle(loadTitle());
                    entryWorksheet.setContent(loadContent());
                    entryWorksheet.setSelfLink(loadGoogleLink());
                    entryWorksheet.setIdGSX(loadIdGSX());
                    entryWorksheet.setPriorityGSX(loadPriorityGSX());
                    skipTag();//device
                    entryWorksheet.setTestCaseNameGSX(loadTestCaseNameGSX());
                    entryWorksheet.setTestCaseDescriptionGSX(loadTestCaseDescriptionGSX());
                    entryWorksheet.setTestStepsGSX(loadTestStepsGSX());
                    entryWorksheet.setLabelGSX(loadLabelGSX());
                    entryWorksheet.setExpectedResultGSX(loadExpectedResultsGSX());
                    skipTag();//gsx:automationstatus
                    skipTag();//gsx:testresult
                    skipTag();//gsx:testresult
                    skipTag();//gsx:testresult
                    skipTag();//gsx:testresult
                    worksheet.setEntryItem(entryWorksheet);
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ENTRY_TAG);
                    Logger.d(TAG, "WORKSHEET ENTRY END");
                    xmlPullParser.nextTag();
                }
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.FEED_TAG);
                Logger.d(TAG, "WORKSHEET END");
                xmlPullParser.next();
            }
            stream.close();
            return worksheet;
        } catch (Throwable t) {
            Logger.e(TAG, "Error loading xml : " + t.toString(), t);
            return null;
        }
    }

}
