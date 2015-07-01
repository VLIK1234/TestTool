package amtt.epam.com.amtt.excel.bo;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GoogleEntryWorksheet extends GoogleEntry {

    private String mIdGSX;
    private String mPriorityGSX;
    private String mDeviceGSX;
    private String mTestCaseNameGSX;
    private String mTestCaseDescriptionGSX;
    private String mTestStepsGSX;
    private String mLabelGSX;
    private String mExpectedResultGSX;
    private String mAutomatisationStatusGSX;
    private String mAndroidGSX;
    private String mAndroidNoteGSX;
    private String mIOSGSX;
    private String mIOSNoteGSX;

    public GoogleEntryWorksheet() {
    }

    public GoogleEntryWorksheet(String idGSX, String priorityGSX, String deviceGSX, String testCaseNameGSX,
                                String testCaseDescriptionGSX, String testStepsGSX, String labelGSX,
                                String expectedResultGSX, String automatisationStatusGSX, String androidGSX,
                                String androidNoteGSX, String iOSGSX, String iOSNoteGSX) {
        this.mIdGSX = idGSX;
        this.mPriorityGSX = priorityGSX;
        this.mDeviceGSX = deviceGSX;
        this.mTestCaseNameGSX = testCaseNameGSX;
        this.mTestCaseDescriptionGSX = testCaseDescriptionGSX;
        this.mTestStepsGSX = testStepsGSX;
        this.mLabelGSX = labelGSX;
        this.mExpectedResultGSX = expectedResultGSX;
        this.mAutomatisationStatusGSX = automatisationStatusGSX;
        this.mAndroidGSX = androidGSX;
        this.mAndroidNoteGSX = androidNoteGSX;
        this.mIOSGSX = iOSGSX;
        this.mIOSNoteGSX = iOSNoteGSX;
    }

    public GoogleEntryWorksheet(String idLink, String updated, String title, GoogleLink selfLink,
                                String content, String idGSX, String priorityGSX, String deviceGSX,
                                String testCaseNameGSX, String testCaseDescriptionGSX, String testStepsGSX,
                                String labelGSX, String expectedResultGSX, String automatisationStatusGSX,
                                String androidGSX, String androidNoteGSX, String iOSGSX, String iOSNoteGSX) {
        super(idLink, updated, title, selfLink, content);
        this.mIdGSX = idGSX;
        this.mPriorityGSX = priorityGSX;
        this.mDeviceGSX = deviceGSX;
        this.mTestCaseNameGSX = testCaseNameGSX;
        this.mTestCaseDescriptionGSX = testCaseDescriptionGSX;
        this.mTestStepsGSX = testStepsGSX;
        this.mLabelGSX = labelGSX;
        this.mExpectedResultGSX = expectedResultGSX;
        this.mAutomatisationStatusGSX = automatisationStatusGSX;
        this.mAndroidGSX = androidGSX;
        this.mAndroidNoteGSX = androidNoteGSX;
        this.mIOSGSX = iOSGSX;
        this.mIOSNoteGSX = iOSNoteGSX;
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

    public String getDeviceGSX() {
        return mDeviceGSX;
    }

    public void setDeviceGSX(String deviceGSX) {
        this.mDeviceGSX = deviceGSX;
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

    public String getAutomatisationStatusGSX() {
        return mAutomatisationStatusGSX;
    }

    public void setAutomatisationStatusGSX(String automatisationStatusGSX) {
        this.mAutomatisationStatusGSX = automatisationStatusGSX;
    }

    public String getAndroidGSX() {
        return mAndroidGSX;
    }

    public void setAndroidGSX(String androidGSX) {
        this.mAndroidGSX = androidGSX;
    }

    public String getAndroidNoteGSX() {
        return mAndroidNoteGSX;
    }

    public void setAndroidNoteGSX(String androidNoteGSX) {
        this.mAndroidNoteGSX = androidNoteGSX;
    }

    public String getIOSGSX() {
        return mIOSGSX;
    }

    public void setIOSGSX(String iOSGSX) {
        this.mIOSGSX = iOSGSX;
    }

    public String getIOSNoteGSX() {
        return mIOSNoteGSX;
    }

    public void setIOSNoteGSX(String iOSNoteGSX) {
        this.mIOSNoteGSX = iOSNoteGSX;
    }
}
