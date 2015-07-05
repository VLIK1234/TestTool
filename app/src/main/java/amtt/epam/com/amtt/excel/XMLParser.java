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
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 02.07.2015
 */

public class XMLParser {

    private static XmlPullParserFactory xmlFactoryObject;
    private static final String TAG = XMLParser.class.getSimpleName();

    public static GoogleSpreadsheet xmlParse(HttpURLConnection conn) {
        try {
            InputStream stream = conn.getInputStream();
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);

            GoogleSpreadsheet spreadshet = new GoogleSpreadsheet();
            myparser.nextTag();
            myparser.require(XmlPullParser.START_TAG, null, "feed");
            while (myparser.nextTag() == XmlPullParser.START_TAG) {
                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ID_TAG);
                spreadshet.setIdLink(myparser.nextText());
                Logger.d(TAG, spreadshet.getIdLink());
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ID_TAG);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.UPDATED_TAG);
                spreadshet.setUpdated(myparser.nextText());
                Logger.d(TAG, spreadshet.getUpdated());
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.UPDATED_TAG);
                myparser.nextTag();
                myparser.nextTag();//skip category
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.TITLE_TAG);
                spreadshet.setTitle(myparser.nextText());
                Logger.d(TAG, spreadshet.getTitle());
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.TITLE_TAG);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                spreadshet = setGoogleLinks(myparser, spreadshet);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                spreadshet = setGoogleLinks(myparser, spreadshet);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                spreadshet = setGoogleLinks(myparser, spreadshet);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
                spreadshet = setGoogleLinks(myparser, spreadshet);
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
                spreadshet.setAuthor(author);
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.EMAIL_TAG);
                myparser.nextTag();
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.AUTHOR_TAG);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.TOTAL_RESULTS_TAG);
                spreadshet.setOpenSearchTotalResults(Integer.parseInt(myparser.nextText()));
                Logger.d(TAG, String.valueOf(spreadshet.getOpenSearchTotalResults()));
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.TOTAL_RESULTS_TAG);
                myparser.nextTag();

                myparser.require(XmlPullParser.START_TAG, null, GoogleApiConst.START_INDEX_TAG);
                spreadshet.setOpenSearchStartIndex(Integer.parseInt(myparser.nextText()));
                Logger.d(TAG, String.valueOf(spreadshet.getOpenSearchStartIndex()));
                myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.START_INDEX_TAG);
                myparser.nextTag();

                for (int i = 0; i < spreadshet.getOpenSearchTotalResults(); i++) {
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
                    spreadshet.setEntryItem(entrySpreadshet);
                    myparser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ENTRY_TAG);
                    myparser.nextTag();
                }

            }
            myparser.require(XmlPullParser.END_TAG, null, "feed");

            stream.close();
            return spreadshet;
        } catch (Throwable t) {
            Logger.e(TAG, "Ошибка при загрузке XML-документа: " + t.toString(), t);
            return null;
        }
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
                    xmlParse(conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
