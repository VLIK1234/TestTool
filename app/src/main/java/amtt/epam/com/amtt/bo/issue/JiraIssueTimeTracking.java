package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 4/23/2015.
 */
public class JiraIssueTimeTracking {

    @SerializedName("originalEstimate")
    private int mOriginalEstimate;
    @SerializedName("remainingEstimate")
    private int mRemainingEstimate;

    public JiraIssueTimeTracking(){}

    public JiraIssueTimeTracking(int originalEstimate, int remainingEstimate) {
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
