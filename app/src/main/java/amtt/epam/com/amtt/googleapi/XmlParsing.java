package amtt.epam.com.amtt.googleapi;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import amtt.epam.com.amtt.googleapi.api.GoogleApiConst;
import amtt.epam.com.amtt.googleapi.bo.GAuthor;
import amtt.epam.com.amtt.googleapi.bo.GLink;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 02.07.2015
 */

public class XmlParsing {

    private static final String TAG = XmlParsing.class.getSimpleName();
    private static XmlPullParser xmlPullParser;

    public static XmlPullParser getXmlPullParser() {
        return xmlPullParser;
    }

    public static void setXmlPullParser(XmlPullParser xmlPullParser) {
        XmlParsing.xmlPullParser = xmlPullParser;
    }

    public static int loadRowCount() throws IOException, XmlPullParserException {
        return readInt(GoogleApiConst.ROW_COUNT_TAG);
    }

    public static int loadColCount() throws IOException, XmlPullParserException {
        return readInt(GoogleApiConst.COL_COUNT_TAG);
    }

    public static String loadContent() throws IOException, XmlPullParserException {
        return readString(GoogleApiConst.CONTENT_TAG);
    }

    public static int loadStartIndex() throws XmlPullParserException, IOException {
        return readInt(GoogleApiConst.START_INDEX_TAG);
    }

    public static int loadTotalResults() throws XmlPullParserException, IOException {
        return readInt(GoogleApiConst.TOTAL_RESULTS_TAG);
    }

    public static GAuthor loadAuthor() throws XmlPullParserException, IOException {
        GAuthor author = new GAuthor();
        xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.AUTHOR_TAG);
        xmlPullParser.nextTag();
        author.setName(readString(GoogleApiConst.NAME_TAG));
        author.setEmail(readString(GoogleApiConst.EMAIL_TAG));
        xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.AUTHOR_TAG);
        xmlPullParser.nextTag();
        return author;
    }

    public static String loadTitle() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.TITLE_TAG);
    }

    public static String loadUpdated() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.UPDATED_TAG);
    }

    public static String loadIdLink() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.ID_TAG);
    }

    public static String loadIdGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.ID_GSX_TAG);
    }

    public static String loadPriorityGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.PRIORITY_GSX_TAG);
    }

    public static String loadTestCaseNameGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.TC_NAME_GSX_TAG);
    }

    public static String loadTestCaseDescriptionGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.TC_DESCRIPTION_GSX_TAG);
    }

    public static String loadTestStepsGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.TEST_STEPS_GSX_TAG);
    }

    public static String loadLabelGSX() throws XmlPullParserException, IOException {
        return readString(GoogleApiConst.LABEL_GSX_TAG);
    }

    public static String loadExpectedResultsGSX() throws XmlPullParserException, IOException {
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

    public static void skipTag() throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
            Logger.d(TAG, "null tag, skip");
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

    public static GLink loadLink() {
        try {
            return loadGoogleLink();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static GLink loadGoogleLink() throws XmlPullParserException, IOException{
        GLink gLink = new GLink();
        xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.LINK_TAG);
        String tag = xmlPullParser.getName();
        String relType = xmlPullParser.getAttributeValue(null, GoogleApiConst.REL_ATTR);
        if (tag.equals(GoogleApiConst.LINK_TAG)) {
            switch (relType) {
                case GoogleApiConst.FEED_ATTR:
                    gLink.setRel(relType);
                    gLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                    break;
                case GoogleApiConst.POST_ATTR:
                    gLink.setRel(relType);
                    gLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                    break;
                case GoogleApiConst.SELF_ATTR:
                    gLink.setRel(relType);
                    gLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                    break;
                case GoogleApiConst.ALTERNATE_ATTR:
                    gLink.setRel(relType);
                    gLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                    break;
                case GoogleApiConst.LISTFEED_ATTR:
                    gLink.setRel(relType);
                    gLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                    break;
                case GoogleApiConst.CELLSFEED_ATTR:
                    gLink.setRel(relType);
                    gLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                    break;
                case GoogleApiConst.VISUALISATION_API_ATTR:
                    gLink.setRel(relType);
                    gLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                    break;
                case GoogleApiConst.EXPORTCSV_ATTR:
                    gLink.setRel(relType);
                    gLink.setHref(xmlPullParser.getAttributeValue(null, GoogleApiConst.HREF_ATTR));
                    break;
            }
        }
        xmlPullParser.nextTag();
        xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.LINK_TAG);
        xmlPullParser.nextTag();
        return gLink;
    }

    public static String loadPathGSX() throws IOException, XmlPullParserException {
        return readString(GoogleApiConst.PATH_GSX_TAG);
    }

    public static String loadSummaryGSX() throws IOException, XmlPullParserException {
        return readString(GoogleApiConst.SUMMARY_GSX_TAG);
    }

    public static String loadTestStatusGSX() throws IOException, XmlPullParserException {
        return readString(GoogleApiConst.STATUS_GSX_TAG);
    }
}
