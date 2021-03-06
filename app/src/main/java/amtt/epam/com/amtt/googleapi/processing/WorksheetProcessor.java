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
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 23.07.2015
 */
public class WorksheetProcessor implements Processor<HttpEntity, GWorksheet> {

    public static final String NAME = WorksheetProcessor.class.getName();

    @Override
    public GWorksheet process(HttpEntity httpEntity) throws Exception {
        try {
            return parseWorksheet(httpEntity);
        } catch (Throwable t) {
            Logger.e(NAME, "Error loading xml : " + t.toString(), t);
            return null;
        }
    }

    @NonNull
    private GWorksheet parseWorksheet(HttpEntity httpEntity) throws IOException, XmlPullParserException {
        InputStream stream = httpEntity.getContent();
        XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = xmlFactoryObject.newPullParser();
        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        xmlPullParser.setInput(stream, null);
        GWorksheet worksheet = new GWorksheet();
        xmlPullParser.nextTag();
        xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.FEED_TAG);
        xmlPullParser.nextTag();
        while (xmlPullParser.getDepth() != 0) {
            XmlParsing.setXmlPullParser(xmlPullParser);
            worksheet.setIdLink(XmlParsing.loadIdLink());
            worksheet.setUpdated(XmlParsing.loadUpdated());
            XmlParsing.skipTag();//category
            worksheet.setTitle(XmlParsing.loadTitle());
            worksheet.setAlternateLink(XmlParsing.loadLink());
            worksheet.setFeedLink(XmlParsing.loadLink());
            worksheet.setPostLink(XmlParsing.loadLink());
            worksheet.setSelfLink(XmlParsing.loadLink());
            worksheet.setAuthor(XmlParsing.loadAuthor());
            worksheet.setOpenSearchTotalResults(XmlParsing.loadTotalResults());
            worksheet.setOpenSearchStartIndex(XmlParsing.loadStartIndex());
            for (int i = 0; i < worksheet.getOpenSearchTotalResults(); i++) {
                GEntryWorksheet entryWorksheet = new GEntryWorksheet();
                xmlPullParser = XmlParsing.getXmlPullParser();
                xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ENTRY_TAG);
                xmlPullParser.nextTag();
                XmlParsing.setXmlPullParser(xmlPullParser);
                entryWorksheet.setIdLink(XmlParsing.loadIdLink());
                entryWorksheet.setUpdated(XmlParsing.loadUpdated());
                XmlParsing.skipTag();
                entryWorksheet.setTitle(XmlParsing.loadTitle());
                entryWorksheet.setContent(XmlParsing.loadContent());
                entryWorksheet.setSelfLink(XmlParsing.loadLink());
                entryWorksheet.setPathGSX(XmlParsing.loadPathGSX());
                entryWorksheet.setPriorityGSX(XmlParsing.loadPriorityGSX());
                entryWorksheet.setTestCaseNameGSX(XmlParsing.loadSummaryGSX());
                entryWorksheet.setTestStepsGSX(XmlParsing.loadTestStepsGSX());
                entryWorksheet.setExpectedResultGSX(XmlParsing.loadExpectedResultsGSX());
                entryWorksheet.setStatusGSX(XmlParsing.loadTestStatusGSX());
                if (entryWorksheet.getTestCaseNameGSX() != null && !entryWorksheet.getTestCaseNameGSX().equals(Constants.Symbols.EMPTY)) {
                    worksheet.setEntryItem(entryWorksheet);
                }
                xmlPullParser = XmlParsing.getXmlPullParser();
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ENTRY_TAG);
                xmlPullParser.nextTag();
            }
            xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.FEED_TAG);
            xmlPullParser.next();
        }
        stream.close();
        return worksheet;
    }
}