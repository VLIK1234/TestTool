package amtt.epam.com.amtt.googleapi.processing;

import android.support.annotation.NonNull;

import org.apache.http.HttpEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import amtt.epam.com.amtt.googleapi.XmlParsing;
import amtt.epam.com.amtt.googleapi.api.GoogleApiConst;
import amtt.epam.com.amtt.googleapi.bo.GEntrySpreadshet;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 07.07.2015
 */
public class SpreadsheetProcessor implements Processor<HttpEntity, GSpreadsheet> {

    public static final String NAME = SpreadsheetProcessor.class.getName();

    @Override
    public GSpreadsheet process(HttpEntity httpEntity) throws Exception {
        try {
            return parseSpreadsheet(httpEntity);
        } catch (Throwable t) {
            Logger.e(NAME, "Error loading xml : " + t.toString(), t);
            return null;
        }
    }

    @NonNull
    private GSpreadsheet parseSpreadsheet(HttpEntity httpEntity) throws IOException, XmlPullParserException {
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
            XmlParsing.setXmlPullParser(xmlPullParser);
            spreadshet.setIdLink(XmlParsing.loadIdLink());
            spreadshet.setUpdated(XmlParsing.loadUpdated());
            XmlParsing.skipTag();//category
            spreadshet.setTitle(XmlParsing.loadTitle());
            spreadshet.setAlternateLink(XmlParsing.loadLink());
            spreadshet.setFeedLink(XmlParsing.loadLink());
            spreadshet.setPostLink(XmlParsing.loadLink());
            spreadshet.setSelfLink(XmlParsing.loadLink());
            spreadshet.setAuthor(XmlParsing.loadAuthor());
            spreadshet.setOpenSearchTotalResults(XmlParsing.loadTotalResults());
            spreadshet.setOpenSearchStartIndex(XmlParsing.loadStartIndex());
            for (int i = 0; i < spreadshet.getOpenSearchTotalResults(); i++) {
                GEntrySpreadshet entrySpreadshet = new GEntrySpreadshet();
                xmlPullParser = XmlParsing.getXmlPullParser();
                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ENTRY_TAG);
                xmlPullParser.nextTag();
                XmlParsing.setXmlPullParser(xmlPullParser);
                entrySpreadshet.setIdLink(XmlParsing.loadIdLink());
                entrySpreadshet.setUpdated(XmlParsing.loadUpdated());
                XmlParsing.skipTag();//category
                entrySpreadshet.setTitle(XmlParsing.loadTitle());
                entrySpreadshet.setContent(XmlParsing.loadContent());
                entrySpreadshet.setListFeedLink(XmlParsing.loadLink());
                entrySpreadshet.setCellsFeedLink(XmlParsing.loadLink());
                entrySpreadshet.setVisualisationApiLink(XmlParsing.loadLink());
                entrySpreadshet.setExportCSVLink(XmlParsing.loadLink());
                entrySpreadshet.setSelfLink(XmlParsing.loadLink());
                entrySpreadshet.setGSColCount(XmlParsing.loadColCount());
                entrySpreadshet.setGSRowCount(XmlParsing.loadRowCount());
                spreadshet.setEntryItem(entrySpreadshet);
                xmlPullParser = XmlParsing.getXmlPullParser();
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ENTRY_TAG);
                xmlPullParser.nextTag();
            }
            xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.FEED_TAG);
            xmlPullParser.next();
        }
        stream.close();
        return spreadshet;
    }
}