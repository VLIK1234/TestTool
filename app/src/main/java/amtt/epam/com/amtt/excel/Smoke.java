package amtt.epam.com.amtt.excel;

/**
 * @author Iryna Monchanka
 * @version on 19.06.2015
 */

public class Smoke {

    private Double mId;
    private String mSummary;
    private String mTestSteps;
    private String mExpectedResult;
    private String mAndroid;
    private String mNotes;
    private String mIOS;
    private String mTestNotes;

    public Double getId() {
        return mId;
    }

    public void setId(Double id) {
        this.mId = id;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public String getTestSteps() {
        return mTestSteps;
    }

    public void setTestSteps(String testSteps) {
        this.mTestSteps = testSteps;
    }

    public String getExpectedResult() {
        return mExpectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.mExpectedResult = expectedResult;
    }

    public String getAndroid() {
        return mAndroid;
    }

    public void setAndroid(String android) {
        this.mAndroid = android;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    public String getIOS() {
        return mIOS;
    }

    public void setIOS(String iOS) {
        this.mIOS = iOS;
    }

    public String getTestNotes() {
        return mTestNotes;
    }

    public void setTestNotes(String testNotes) {
        this.mTestNotes = testNotes;
    }
}
