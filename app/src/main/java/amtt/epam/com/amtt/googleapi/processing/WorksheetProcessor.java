package amtt.epam.com.amtt.googleapi.processing;

import org.apache.http.HttpEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

import amtt.epam.com.amtt.googleapi.XMLParser;
import amtt.epam.com.amtt.googleapi.api.GoogleApiConst;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 07.07.2015
 */
public class WorksheetProcessor implements Processor<GWorksheet, HttpEntity> {

    public static final String NAME = WorksheetProcessor.class.getName();

    @Override
    public GWorksheet process(HttpEntity httpEntity) throws Exception {
        try {
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
                XMLParser.setXmlPullParser(xmlPullParser);
                worksheet.setIdLink(XMLParser.loadIdLink());
                worksheet.setUpdated(XMLParser.loadUpdated());
                XMLParser.skipTag();//category
                worksheet.setTitle(XMLParser.loadTitle());
                worksheet.setAlternateLink(XMLParser.loadGoogleLink());
                worksheet.setFeedLink(XMLParser.loadGoogleLink());
                worksheet.setPostLink(XMLParser.loadGoogleLink());
                worksheet.setSelfLink(XMLParser.loadGoogleLink());
                worksheet.setAuthor(XMLParser.loadAuthor());
                worksheet.setOpenSearchTotalResults(XMLParser.loadTotalResults());
                worksheet.setOpenSearchStartIndex(XMLParser.loadStartIndex());
                for (int i = 0; i < worksheet.getOpenSearchTotalResults(); i++) {
                    GEntryWorksheet entryWorksheet = new GEntryWorksheet();
                    xmlPullParser = XMLParser.getXmlPullParser();
                    xmlPullParser.require(XmlPullParser.START_TAG, null, GoogleApiConst.ENTRY_TAG);
                    xmlPullParser.nextTag();
                    XMLParser.setXmlPullParser(xmlPullParser);
                    entryWorksheet.setIdLink(XMLParser.loadIdLink());
                    entryWorksheet.setUpdated(XMLParser.loadUpdated());
                    XMLParser.skipTag();
                    entryWorksheet.setTitle(XMLParser.loadTitle());
                    entryWorksheet.setContent(XMLParser.loadContent());
                    entryWorksheet.setSelfLink(XMLParser.loadGoogleLink());
                    entryWorksheet.setIdGSX(XMLParser.loadIdGSX());
                    entryWorksheet.setPriorityGSX(XMLParser.loadPriorityGSX());
                    XMLParser.skipTag();//device
                    entryWorksheet.setTestCaseNameGSX(XMLParser.loadTestCaseNameGSX());
                    entryWorksheet.setTestCaseDescriptionGSX(XMLParser.loadTestCaseDescriptionGSX());
                    entryWorksheet.setTestStepsGSX(XMLParser.loadTestStepsGSX());
                    entryWorksheet.setLabelGSX(XMLParser.loadLabelGSX());
                    entryWorksheet.setExpectedResultGSX(XMLParser.loadExpectedResultsGSX());
                    XMLParser.skipTag();//gsx:automationstatus
                    XMLParser.skipTag();//gsx:testresult
                    XMLParser.skipTag();//gsx:testresult
                    XMLParser.skipTag();//gsx:testresult
                    XMLParser.skipTag();//gsx:testresult
                    if (entryWorksheet.getTestCaseNameGSX() != null && !entryWorksheet.getTestCaseNameGSX().equals(Constants.Symbols.EMPTY)) {
                        worksheet.setEntryItem(entryWorksheet);
                        GSpreadsheetContent.getInstance().setTag(entryWorksheet.getTestCaseNameGSX(), entryWorksheet.getIdLink());
                    }
                    xmlPullParser = XMLParser.getXmlPullParser();
                    xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.ENTRY_TAG);
                    xmlPullParser.nextTag();
                }
                xmlPullParser.require(XmlPullParser.END_TAG, null, GoogleApiConst.FEED_TAG);
                xmlPullParser.next();
            }
            stream.close();
            return worksheet;
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