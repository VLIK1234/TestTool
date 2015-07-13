package amtt.epam.com.amtt.excel.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GoogleEntryWorksheet extends GoogleEntry {

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

}
