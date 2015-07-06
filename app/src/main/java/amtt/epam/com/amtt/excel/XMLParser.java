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

    public static GoogleSpreadsheet xmlParse(HttpURLConnection conn) {
        try {
            InputStream stream = conn.getInputStream();
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlFactoryObject.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(stream, null);

            GoogleSpreadsheet spreadshet = new GoogleSpreadsheet();
            xmlPullParser.nextTag();
            xmlPullParser.require(XmlPullParser.START_TAG, null, "feed");
            while (xmlPullParser.nextTag() == XmlPullParser.START_TAG) {
                spreadshet.setIdLink(loadIdLink());
                spreadshet.setUpdated(loadUpdated());

                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.TITLE_TAG);
                spreadshet.setTitle(xmlPullParser.nextText());
                Logger.d(TAG, spreadshet.getTitle());
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.TITLE_TAG);
                xmlPullParser.nextTag();

                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                spreadshet = setGoogleLinks(xmlPullParser, spreadshet);
                xmlPullParser.nextTag();

                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                spreadshet = setGoogleLinks(xmlPullParser, spreadshet);
                xmlPullParser.nextTag();

                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                spreadshet = setGoogleLinks(xmlPullParser, spreadshet);
                xmlPullParser.nextTag();

                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                spreadshet = setGoogleLinks(xmlPullParser, spreadshet);
                xmlPullParser.nextTag();

                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.AUTHOR_TAG);
                GoogleAuthor author = new GoogleAuthor();
                xmlPullParser.nextTag();
                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.NAME_TAG);
                author.setName(xmlPullParser.nextText());
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.NAME_TAG);
                xmlPullParser.nextTag();
                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.EMAIL_TAG);
                author.setEmail(xmlPullParser.nextText());
                spreadshet.setAuthor(author);
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.EMAIL_TAG);
                xmlPullParser.nextTag();
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.AUTHOR_TAG);
                xmlPullParser.nextTag();

                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.TOTAL_RESULTS_TAG);
                spreadshet.setOpenSearchTotalResults(Integer.parseInt(xmlPullParser.nextText()));
                Logger.d(TAG, String.valueOf(spreadshet.getOpenSearchTotalResults()));
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.TOTAL_RESULTS_TAG);
                xmlPullParser.nextTag();

                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.START_INDEX_TAG);
                spreadshet.setOpenSearchStartIndex(Integer.parseInt(xmlPullParser.nextText()));
                Logger.d(TAG, String.valueOf(spreadshet.getOpenSearchStartIndex()));
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.START_INDEX_TAG);
                xmlPullParser.nextTag();

                for (int i = 0; i < spreadshet.getOpenSearchTotalResults(); i++) {
                    GoogleEntrySpreadshet entrySpreadshet = new GoogleEntrySpreadshet();
                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ENTRY_TAG);
                    xmlPullParser.nextTag();
                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ID_TAG);
                    entrySpreadshet.setIdLink(xmlPullParser.nextText());
                    Logger.d(TAG, entrySpreadshet.getIdLink());
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ID_TAG);
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.UPDATED_TAG);
                    entrySpreadshet.setUpdated(xmlPullParser.nextText());
                    Logger.d(TAG, entrySpreadshet.getUpdated());
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.UPDATED_TAG);
                    xmlPullParser.nextTag();
                    xmlPullParser.nextTag();//skip category
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.TITLE_TAG);
                    entrySpreadshet.setTitle(xmlPullParser.nextText());
                    Logger.d(TAG, entrySpreadshet.getTitle());
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.TITLE_TAG);
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.CONTENT_TAG);
                    entrySpreadshet.setTitle(xmlPullParser.nextText());
                    Logger.d(TAG, entrySpreadshet.getTitle());
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.CONTENT_TAG);
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(xmlPullParser, entrySpreadshet);
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(xmlPullParser, entrySpreadshet);
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(xmlPullParser, entrySpreadshet);
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(xmlPullParser, entrySpreadshet);
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(xmlPullParser, entrySpreadshet);
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.COL_COUNT_TAG);
                    entrySpreadshet.setGSColCount(Integer.parseInt(xmlPullParser.nextText()));
                    Logger.d(TAG, String.valueOf(entrySpreadshet.getGSColCount()));
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.COL_COUNT_TAG);
                    xmlPullParser.nextTag();

                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ROW_COUNT_TAG);
                    entrySpreadshet.setGSRowCount(Integer.parseInt(xmlPullParser.nextText()));
                    Logger.d(TAG, String.valueOf(entrySpreadshet.getGSRowCount()));
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ROW_COUNT_TAG);
                    xmlPullParser.nextTag();
                    spreadshet.setEntryItem(entrySpreadshet);
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ENTRY_TAG);
                    xmlPullParser.nextTag();
                }

            }
            xmlPullParser.require(XmlPullParser.END_TAG, null, "feed");

            stream.close();
            return spreadshet;
        } catch (Throwable t) {
            Logger.e(TAG, "Ошибка при загрузке XML-документа: " + t.toString(), t);
            return null;
        }
    }

    private static String loadUpdated() throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.UPDATED_TAG);
        String updated = xmlPullParser.nextText();
        xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.UPDATED_TAG);
        xmlPullParser.nextTag();
        xmlPullParser.nextTag();//skip category
        xmlPullParser.nextTag();
        return updated;
    }

    private static String loadIdLink() throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ID_TAG);
        String id = xmlPullParser.nextText();
        xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ID_TAG);
        xmlPullParser.nextTag();
        return id;
    }

    private static GoogleSpreadsheet setGoogleLinks(XmlPullParser resourse, GoogleSpreadsheet spreadshet) {
        GoogleLink googleLink = new GoogleLink();
        try {
            resourse.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);

            String tag = resourse.getName();
            String relType = resourse.getAttributeValue(null, GoogleApiConst.REL_ATTR);
            if (tag.equals(GoogleApiConst.LINK_TAG)) {
                switch (relType) {
                    case GoogleApiConst.FEED_ATTR:
                        googleLink.setRel(relType);
                        Logger.e(TAG, relType);
                        googleLink.setHref(resourse.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        resourse.nextTag();
                        spreadshet.setFeedLink(googleLink);
                        break;
                    case GoogleApiConst.POST_ATTR:
                        googleLink.setRel(relType);
                        Logger.e(TAG, relType);
                        googleLink.setHref(resourse.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        resourse.nextTag();
                        spreadshet.setPostLink(googleLink);
                        break;
                    case GoogleApiConst.SELF_ATTR:
                        googleLink.setRel(relType);
                        Logger.e(TAG, relType);
                        googleLink.setHref(resourse.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        resourse.nextTag();
                        spreadshet.setSelfLink(googleLink);
                        break;
                    case GoogleApiConst.ALTERNATE_ATTR:
                        googleLink.setRel(relType);
                        Logger.e(TAG, relType);
                        googleLink.setHref(resourse.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        resourse.nextTag();
                        spreadshet.setAlternateLink(googleLink);
                        break;
                }
            }
            resourse.require(XmlPullParser.END_TAG, null, GoogleApiConst.LINK_TAG);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return spreadshet;
    }

    private static GoogleEntrySpreadshet setGoogleLinks(XmlPullParser resourse, GoogleEntrySpreadshet spreadshet) {
        GoogleLink googleLink = new GoogleLink();
        try {
            resourse.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);

            String tag = resourse.getName();
            String relType = resourse.getAttributeValue(null, GoogleApiConst.REL_ATTR);
            if (tag.equals(GoogleApiConst.LINK_TAG)) {
                switch (relType) {
                    case GoogleApiConst.LISTFEED_ATTR:
                        googleLink.setRel(relType);
                        Logger.e(TAG, relType);
                        googleLink.setHref(resourse.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        resourse.nextTag();
                        spreadshet.setListFeedLink(googleLink);
                        break;
                    case GoogleApiConst.CELLSFEED_ATTR:
                        googleLink.setRel(relType);
                        Logger.e(TAG, relType);
                        googleLink.setHref(resourse.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        resourse.nextTag();
                        spreadshet.setCellsFeedLink(googleLink);
                        break;
                    case GoogleApiConst.SELF_ATTR:
                        googleLink.setRel(relType);
                        Logger.e(TAG, relType);
                        googleLink.setHref(resourse.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        resourse.nextTag();
                        spreadshet.setSelfLink(googleLink);
                        break;
                    case GoogleApiConst.VISUALISATION_API_ATTR:
                        googleLink.setRel(relType);
                        Logger.e(TAG, relType);
                        googleLink.setHref(resourse.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        resourse.nextTag();
                        spreadshet.setVisualisationApiLink(googleLink);
                        break;
                    case GoogleApiConst.EXPORTCSV_ATTR:
                        googleLink.setRel(relType);
                        Logger.e(TAG, relType);
                        googleLink.setHref(resourse.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                        resourse.nextTag();
                        spreadshet.setExportCSVLink(googleLink);
                        break;
                }
            }
            resourse.require(XmlPullParser.END_TAG, null, GoogleApiConst.LINK_TAG);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return spreadshet;
    }

    public static void fetchXML() {
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
                    GoogleSpreadsheet spreadsheet = xmlParse(conn);
                    if (spreadsheet != null) {
                        URL url2 = new URL(spreadsheet.getListWorksheets().get(3));
                        HttpURLConnection con = (HttpURLConnection) url2.openConnection();
                        con.setReadTimeout(10000 /* milliseconds */);
                        con.setConnectTimeout(15000 /* milliseconds */);
                        con.setRequestMethod("GET");
                        con.setDoInput(true);
                        con.connect();
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
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);

            GoogleWorksheet worksheet = new GoogleWorksheet();
            myparser.nextTag();
            myparser.require(XmlPullParser.START_TAG, null, "feed");
            while (myparser.nextTag() == XmlPullParser.START_TAG) {
                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ID_TAG);
                worksheet.setIdLink(myparser.nextText());
                Logger.d(TAG, worksheet.getIdLink());
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ID_TAG);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.UPDATED_TAG);
                worksheet.setUpdated(myparser.nextText());
                Logger.d(TAG, worksheet.getUpdated());
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.UPDATED_TAG);
                myparser.nextTag();
                myparser.nextTag();//skip category
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.TITLE_TAG);
                worksheet.setTitle(myparser.nextText());
                Logger.d(TAG, worksheet.getTitle());
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.TITLE_TAG);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                worksheet = setGoogleLinks(myparser, worksheet);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                worksheet = setGoogleLinks(myparser, worksheet);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                worksheet = setGoogleLinks(myparser, worksheet);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                worksheet = setGoogleLinks(myparser, worksheet);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.AUTHOR_TAG);
                GoogleAuthor author = new GoogleAuthor();
                myparser.nextTag();
                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.NAME_TAG);
                author.setName(myparser.nextText());
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.NAME_TAG);
                myparser.nextTag();
                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.EMAIL_TAG);
                author.setEmail(myparser.nextText());
                worksheet.setAuthor(author);
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.EMAIL_TAG);
                myparser.nextTag();
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.AUTHOR_TAG);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.TOTAL_RESULTS_TAG);
                worksheet.setOpenSearchTotalResults(Integer.parseInt(myparser.nextText()));
                Logger.d(TAG, String.valueOf(worksheet.getOpenSearchTotalResults()));
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.TOTAL_RESULTS_TAG);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.START_INDEX_TAG);
                worksheet.setOpenSearchStartIndex(Integer.parseInt(myparser.nextText()));
                Logger.d(TAG, String.valueOf(worksheet.getOpenSearchStartIndex()));
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.START_INDEX_TAG);
                myparser.nextTag();

                for (int i = 0; i < worksheet.getOpenSearchTotalResults(); i++) {
                    GoogleEntrySpreadshet entrySpreadshet = new GoogleEntrySpreadshet();
                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ENTRY_TAG);
                    myparser.nextTag();
                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ID_TAG);
                    entrySpreadshet.setIdLink(myparser.nextText());
                    Logger.d(TAG, entrySpreadshet.getIdLink());
                    myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ID_TAG);
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.UPDATED_TAG);
                    entrySpreadshet.setUpdated(myparser.nextText());
                    Logger.d(TAG, entrySpreadshet.getUpdated());
                    myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.UPDATED_TAG);
                    myparser.nextTag();
                    myparser.nextTag();//skip category
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.TITLE_TAG);
                    entrySpreadshet.setTitle(myparser.nextText());
                    Logger.d(TAG, entrySpreadshet.getTitle());
                    myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.TITLE_TAG);
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.CONTENT_TAG);
                    entrySpreadshet.setTitle(myparser.nextText());
                    Logger.d(TAG, entrySpreadshet.getTitle());
                    myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.CONTENT_TAG);
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(myparser, entrySpreadshet);
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(myparser, entrySpreadshet);
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(myparser, entrySpreadshet);
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(myparser, entrySpreadshet);
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                    entrySpreadshet = setGoogleLinks(myparser, entrySpreadshet);
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.COL_COUNT_TAG);
                    entrySpreadshet.setGSColCount(Integer.parseInt(myparser.nextText()));
                    Logger.d(TAG, String.valueOf(entrySpreadshet.getGSColCount()));
                    myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.COL_COUNT_TAG);
                    myparser.nextTag();

                    myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ROW_COUNT_TAG);
                    entrySpreadshet.setGSRowCount(Integer.parseInt(myparser.nextText()));
                    Logger.d(TAG, String.valueOf(entrySpreadshet.getGSRowCount()));
                    myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ROW_COUNT_TAG);
                    myparser.nextTag();
                    worksheet.setEntryItem(entrySpreadshet);
                    myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ENTRY_TAG);
                    myparser.nextTag();
                }

            }
            myparser.require(XmlPullParser.END_TAG, null, "feed");

            stream.close();
            return worksheet;
        } catch (Throwable t) {
            Logger.e(TAG, "Ошибка при загрузке XML-документа: " + t.toString(), t);
            return null;
        }
    }

}
