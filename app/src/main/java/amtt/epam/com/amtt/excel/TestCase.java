package amtt.epam.com.amtt.excel;

/**
 * @author Iryna Monchanka
 * @version on 19.06.2015
 */

public class TestCase {

    private Double mId;
    private String mPriority;
    private String mTestCaseName;
    private String mTestCaseDescription;
    private String mTestSteps;
    private String mExpectedResult;

    public Double getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getTestCaseName() {
        return mTestCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.mTestCaseName = testCaseName;
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

    public String getTestCaseDescription() {
        return mTestCaseDescription;
    }

    public void setTestCaseDescription(String testCaseDescription) {
        this.mTestCaseDescription = testCaseDescription;
    }

    public String getPriority() {
        return mPriority;
    }

    public void setPriority(String priority) {
        this.mPriority = priority;
    }
}
