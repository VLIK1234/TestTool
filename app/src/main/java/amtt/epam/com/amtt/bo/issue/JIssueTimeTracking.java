package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 @author Iryna Monchanka
 @version on 4/23/2015
 */

public class JIssueTimeTracking {

    @SerializedName("originalEstimate")
    private int mOriginalEstimate;
    @SerializedName("remainingEstimate")
    private int mRemainingEstimate;

    public JIssueTimeTracking(){}

    public JIssueTimeTracking(int originalEstimate, int remainingEstimate) {
        this.mOriginalEstimate = originalEstimate;
        this.mRemainingEstimate = remainingEstimate;
    }

    public int getOriginalEstimate() {
        return mOriginalEstimate;
    }

    public void setOriginalEstimate(int originalEstimate) {
        this.mOriginalEstimate = originalEstimate;
    }

    public int getRemainingEstimate() {
        return mRemainingEstimate;
    }

    public void setRemainingEstimate(int remainingEstimate) {
        this.mRemainingEstimate = remainingEstimate;
    }
}
