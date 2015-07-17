package amtt.epam.com.amtt.excel.bo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.excel.database.contentprovider.GSUri;
import amtt.epam.com.amtt.excel.database.table.TestcaseTable;
import amtt.epam.com.amtt.util.Constants;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GoogleEntryWorksheet extends GoogleEntry<GoogleEntryWorksheet> {

    private int mId;
    private String mIdWorksheetLink;
    private String mIdGSX;
    private String mPriorityGSX;
   // private String mDeviceGSX;
    private String mTestCaseNameGSX;
    private String mTestCaseDescriptionGSX;
    private String mTestStepsGSX;
    private String mLabelGSX;
    private String mExpectedResultGSX;
   // private String mAutomatisationStatusGSX;
   // private String mAndroidGSX;
   // private String mAndroidNoteGSX;
   // private String mIOSGSX;
   // private String mIOSNoteGSX;

    public GoogleEntryWorksheet() {
    }

    public GoogleEntryWorksheet(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(TestcaseTable._ID));
        mIdLink = cursor.getString(cursor.getColumnIndex(TestcaseTable._TESTCASE_ID_LINK));
        mIdWorksheetLink = cursor.getString(cursor.getColumnIndex(TestcaseTable._WORKSHEET_ID_LINK));
        mUpdated = cursor.getString(cursor.getColumnIndex(TestcaseTable._UPDATED));
        mTitle = cursor.getString(cursor.getColumnIndex(TestcaseTable._TITLE));
        mIdGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._TESTCASE_ID));
        mPriorityGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._PRIORITY));
        mTestCaseNameGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._NAME));
        mTestCaseDescriptionGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._DESCRIPTION));
        mTestStepsGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._STEPS));
        mLabelGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._LABEL));
        mExpectedResultGSX = cursor.getString(cursor.getColumnIndex(TestcaseTable._EXPECTED_RESULTS));
    }

    @Override
    public GoogleEntryWorksheet parse(Cursor cursor) {
        return null;
    }

    public GoogleEntryWorksheet(String idGSX, String priorityGSX, String testCaseNameGSX,
                                String testCaseDescriptionGSX, String testStepsGSX, String labelGSX,
                                String expectedResultGSX) {
        this.mIdGSX = idGSX;
        this.mPriorityGSX = priorityGSX;
        //this.mDeviceGSX = deviceGSX;
        this.mTestCaseNameGSX = testCaseNameGSX;
        this.mTestCaseDescriptionGSX = testCaseDescriptionGSX;
        this.mTestStepsGSX = testStepsGSX;
        this.mLabelGSX = labelGSX;
        this.mExpectedResultGSX = expectedResultGSX;
        //this.mAutomatisationStatusGSX = automatisationStatusGSX;
       // this.mAndroidGSX = androidGSX;
       // this.mAndroidNoteGSX = androidNoteGSX;
        //this.mIOSGSX = iOSGSX;
       // this.mIOSNoteGSX = iOSNoteGSX;
    }

    public GoogleEntryWorksheet(String idLink, String updated, String title, GoogleLink selfLink,
                                String content, String idGSX, String priorityGSX, String testCaseNameGSX,
                                String testCaseDescriptionGSX, String testStepsGSX,String labelGSX,
                                String expectedResultGSX) {
        super(idLink, updated, title, selfLink, content);
        this.mIdGSX = idGSX;
        this.mPriorityGSX = priorityGSX;
        //this.mDeviceGSX = deviceGSX;
        this.mTestCaseNameGSX = testCaseNameGSX;
        this.mTestCaseDescriptionGSX = testCaseDescriptionGSX;
        this.mTestStepsGSX = testStepsGSX;
        this.mLabelGSX = labelGSX;
        this.mExpectedResultGSX = expectedResultGSX;
        /*this.mAutomatisationStatusGSX = automatisationStatusGSX;
        this.mAndroidGSX = androidGSX;
        this.mAndroidNoteGSX = androidNoteGSX;
        this.mIOSGSX = iOSGSX;
        this.mIOSNoteGSX = iOSNoteGSX;*/
    }

    public String getIdGSX() {
        return mIdGSX;
    }

    public void setIdGSX(String idGSX) {
        this.mIdGSX = idGSX;
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

    public String getTestcaseNameAndId() {
        String nameAndId = null;
        if (mTestCaseNameGSX != null) {
            nameAndId = mTestCaseNameGSX + Constants.Symbols.ID_LEFT_BRACKET
                    + mIdGSX + Constants.Symbols.ID_RIGHT_BRACKET;
        }
        return nameAndId;
    }

    public String getFullTestCaseDescription(String newSteps){
        String fullDescription = getFullTestCaseDescription();

        return fullDescription;
    }
    public String getFullTestCaseDescription(){
        Context context = AmttApplication.getContext();
        SpannableStringBuilder fullDescription = new SpannableStringBuilder();
        fullDescription.append(Html.fromHtml("<h5>" + context.getString(R.string.label_steps) + "</h5>"));
        fullDescription.append(Html.fromHtml("</br>" + mTestCaseDescriptionGSX + "</br>"));
        fullDescription.append(Html.fromHtml("</br>" + mTestCaseDescriptionGSX + "</br>"));
        return String.valueOf(fullDescription);
    }

    public String getTestCaseDescriptionGSX() {
        return mTestCaseDescriptionGSX;
    }

    public void setTestCaseDescriptionGSX(String testCaseDescriptionGSX) {
        this.mTestCaseDescriptionGSX = testCaseDescriptionGSX;
    }

    public String getTestStepsGSX() {
        return mTestStepsGSX;
    }

    public void setTestStepsGSX(String testStepsGSX) {
        this.mTestStepsGSX = testStepsGSX;
    }

    public String getLabelGSX() {
        return mLabelGSX;
    }

    public void setLabelGSX(String labelGSX) {
        this.mLabelGSX = labelGSX;
    }

    public String getExpectedResultGSX() {
        return mExpectedResultGSX;
    }

    public void setExpectedResultGSX(String expectedResultGSX) {
        this.mExpectedResultGSX = expectedResultGSX;
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
        values.put(TestcaseTable._TESTCASE_ID, mIdGSX);
        values.put(TestcaseTable._PRIORITY, mPriorityGSX);
        values.put(TestcaseTable._NAME, mTestCaseNameGSX);
        values.put(TestcaseTable._DESCRIPTION, mTestCaseDescriptionGSX);
        values.put(TestcaseTable._STEPS, mTestStepsGSX);
        values.put(TestcaseTable._LABEL, mLabelGSX);
        values.put(TestcaseTable._EXPECTED_RESULTS, mExpectedResultGSX);
        return values;
    }
}
