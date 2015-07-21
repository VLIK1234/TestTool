package amtt.epam.com.amtt.googleapi.processing;

import org.apache.http.HttpEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

import amtt.epam.com.amtt.googleapi.XMLParser;
import amtt.epam.com.amtt.googleapi.api.GoogleApiConst;
import amtt.epam.com.amtt.googleapi.bo.GEntrySpreadshet;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 07.07.2015
 */
public class SpreadsheetProcessor implements Processor<GSpreadsheet, HttpEntity> {

    public static final String NAME = SpreadsheetProcessor.class.getName();

    @Override
    public GSpreadsheet process(HttpEntity httpEntity) throws Exception {
        try {
            InputStream stream = httpEntity.getContent();
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlFactoryObject.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(stream, null);
            GSpreadsheet spreadshet = new GSpreadsheet();
            xmlPullParser.nextTag();
            xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.FEED_TAG);
            xmlPullParser.nextTag();
            while (xmlPullParser.getDepth() != 0) {
                XMLParser.setXmlPullParser(xmlPullParser);
                spreadshet.setIdLink(XMLParser.loadIdLink());
                spreadshet.setUpdated(XMLParser.loadUpdated());
                XMLParser.skipTag();//category
                spreadshet.setTitle(XMLParser.loadTitle());
                spreadshet.setAlternateLink(XMLParser.loadGoogleLink());
                spreadshet.setFeedLink(XMLParser.loadGoogleLink());
                spreadshet.setPostLink(XMLParser.loadGoogleLink());
                spreadshet.setSelfLink(XMLParser.loadGoogleLink());
                spreadshet.setAuthor(XMLParser.loadAuthor());
                spreadshet.setOpenSearchTotalResults(XMLParser.loadTotalResults());
                spreadshet.setOpenSearchStartIndex(XMLParser.loadStartIndex());
                for (int i = 0; i < spreadshet.getOpenSearchTotalResults(); i++) {
                    GEntrySpreadshet entrySpreadshet = new GEntrySpreadshet();
                    xmlPullParser = XMLParser.getXmlPullParser();
                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ENTRY_TAG);
                    xmlPullParser.nextTag();
                    XMLParser.setXmlPullParser(xmlPullParser);
                    entrySpreadshet.setIdLink(XMLParser.loadIdLink());
                    entrySpreadshet.setUpdated(XMLParser.loadUpdated());
                    XMLParser.skipTag();//category
                    entrySpreadshet.setTitle(XMLParser.loadTitle());
                    entrySpreadshet.setContent(XMLParser.loadContent());
                    entrySpreadshet.setListFeedLink(XMLParser.loadGoogleLink());
                    entrySpreadshet.setCellsFeedLink(XMLParser.loadGoogleLink());
                    entrySpreadshet.setVisualisationApiLink(XMLParser.loadGoogleLink());
                    entrySpreadshet.setExportCSVLink(XMLParser.loadGoogleLink());
                    entrySpreadshet.setSelfLink(XMLParser.loadGoogleLink());
                    entrySpreadshet.setGSColCount(XMLParser.loadColCount());
                    entrySpreadshet.setGSRowCount(XMLParser.loadRowCount());
                    spreadshet.setEntryItem(entrySpreadshet);
                    xmlPullParser = XMLParser.getXmlPullParser();
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ENTRY_TAG);
                    xmlPullParser.nextTag();
                }
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.FEED_TAG);
                xmlPullParser.next();
            }
            stream.close();
            return spreadshet;
        } catch (Throwable t) {
            Logger.e(NAME, "Error loading xml : " + t.toString(), t);
            return null;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}