package amtt.epam.com.amtt.googleapi.bo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.googleapi.database.contentprovider.GSUri;
import amtt.epam.com.amtt.googleapi.database.table.TestcaseTable;
import amtt.epam.com.amtt.util.Constants;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GEntryWorksheet extends GEntry<GEntryWorksheet> {

    private int mId;
    private String mIdWorksheetLink;
    private String mPriorityGSX;
    // private String mDeviceGSX;
    private String mTestCaseNameGSX;
    private String mTestStepsGSX;
    private String mExpectedResultGSX;
    private String mPathGSX;
    private String mStatusGSX;

   /** for parsing https://docs.google.com/spreadsheets/d/1tUoL9M-2HZOmxL8W83fiRyGJ35iabXQ4CEpM3A2IlC4/edit#gid=1269796344
    *
    * private String mLabelGSX;
    * private String mTestCaseDescriptionGSX;
    * private String mIdGSX;
   // private String mAutomatisationStatusGSX;
   // private String mAndroidGSX;
   // private String mAndroidNoteGSX;
   // private String mIOSGSX;
   // private String mIOSNoteGSX;*/

    public GEntryWorksheet() {
    }

    public GEntryWorksheet(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(TestcaseTable._ID));
        mIdLink = cursor.getString(cursor.getColumnIndex(TestcaseTable._TESTCASE_ID_LINK));
        mIdWorksheetLink = cursor.getString(cursor.getColumnIndex(TestcaseTable._WORKSHEET_ID_LINK));
        mUpdated = cursor.getString(cursor.getColumnIndex(TestcaseTable._UPDATED));
        mTitle = cursor.getString(cursor.getColumnIndex(TestcaseTable._TITLE));
        mPriorityGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._PRIORITY));
        mTestCaseNameGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._NAME));
        mTestStepsGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._STEPS));
        mExpectedResultGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._EXPECTED_RESULTS));
        mPathGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._PATH));
        mStatusGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._STATUS));
    }

    @Override
    public GEntryWorksheet parse(Cursor cursor) {
        return new GEntryWorksheet(cursor);
    }

    public String getPriorityGSX() {
        return mPriorityGSX;
    }

    public void setPriorityGSX(String priorityGSX) {
        this.mPriorityGSX = priorityGSX;
    }

    public String getTestCaseNameGSX() {
        return mTestCaseNameGSX;
    }

    public void setTestCaseNameGSX(String testCaseNameGSX) {
        this.mTestCaseNameGSX = testCaseNameGSX;
    }

    public String getIdWorksheetLink() {
        return mIdWorksheetLink;
    }

    public void setIdWorksheetLink(String idWorksheetLink) {
        this.mIdWorksheetLink = idWorksheetLink;
    }

    public SpannableStringBuilder getFullTestCaseDescription(Spanned newSteps){
        SpannableStringBuilder fullDescription = new SpannableStringBuilder();
        if (getFullTestCaseDescription() != null) {
            fullDescription = getFullTestCaseDescription();
        }
        if(newSteps != null){
            fullDescription.append(Html.fromHtml("<br/>" + "<br/>" + "<h5>" + "New steps : "
                    + "</h5>"));
            fullDescription.append(newSteps);
        }
        return fullDescription;
    }

    public String getPathGSX() {
        return mPathGSX;
    }

    public void setPathGSX(String pathGSX) {
        mPathGSX = pathGSX;
    }

    public String getStatusGSX() {
        return mStatusGSX;
    }

    public void setStatusGSX(String statusGSX) {
        mStatusGSX = statusGSX;
    }

    public SpannableStringBuilder getFullTestCaseDescription(){
        Context context = AmttApplication.getContext();
        SpannableStringBuilder fullDescription = new SpannableStringBuilder();
        fullDescription.append(Html.fromHtml("<h5>" + context.getString(R.string.label_steps) + "</h5>"));
        fullDescription.append(Html.fromHtml(mTestStepsGSX));
        fullDescription.append(Html.fromHtml("<br/>"+"<h5>" + context.getString(R.string.label_expected_result) + "</h5>"));
        fullDescription.append(Html.fromHtml(mExpectedResultGSX));
        return fullDescription;
    }

    public String getTestStepsGSX() {
        return mTestStepsGSX;
    }

    public void setTestStepsGSX(String testStepsGSX) {
        this.mTestStepsGSX = testStepsGSX;
    }

    public String getExpectedResultGSX() {
        return mExpectedResultGSX;
    }

    public void setExpectedResultGSX(String expectedResultGSX) {
        this.mExpectedResultGSX = expectedResultGSX;
    }

    public List<GTag> getTags() {
        if (mTestCaseNameGSX != null && mIdLink != null) {
            GTag gTag;
            List<GTag> mTags = new ArrayList<>();
            String[] tags = mTestCaseNameGSX.split(" - ");
            for (String tag : tags) {
                gTag = new GTag();
                gTag.setName(tag);
                gTag.setIdLinkTestCase(mIdLink);
                mTags.add(gTag);
            }
            return mTags;
        } else {
            return null;
        }
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Uri getUri() {
        return GSUri.TESTCASE.get();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(TestcaseTable._TESTCASE_ID_LINK, mIdLink);
        values.put(TestcaseTable._WORKSHEET_ID_LINK, mIdWorksheetLink);
        values.put(TestcaseTable._UPDATED, mUpdated);
        values.put(TestcaseTable._TITLE, mTitle);
        values.put(TestcaseTable._PRIORITY, mPriorityGSX);
        values.put(TestcaseTable._NAME, mTestCaseNameGSX);
        values.put(TestcaseTable._STEPS, mTestStepsGSX);
        values.put(TestcaseTable._EXPECTED_RESULTS, mExpectedResultGSX);
        values.put(TestcaseTable._PATH, mPathGSX);
        values.put(TestcaseTable._STATUS, mStatusGSX);
        return values;
    }
}
