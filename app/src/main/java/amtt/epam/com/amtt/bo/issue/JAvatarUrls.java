package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 @author Iryna Monchanka
 @version on 30.03.2015
 */

public class JAvatarUrls {

    @SerializedName("48x48")
    private final String mAvatarUrl;
    @SerializedName("24x24")
    private final String mAvatarSmallUrl;
    @SerializedName("16x16")
    private final String mAvatarXSmallUrl;
    @SerializedName("32x32")
    private final String mAvatarMediumUrl;

    public JAvatarUrls(String avatarUrl, String avatarSmallUrl, String avatarXSmallUrl, String avatarMediumUrl) {
        this.mAvatarUrl = avatarUrl;
        this.mAvatarSmallUrl = avatarSmallUrl;
        this.mAvatarXSmallUrl = avatarXSmallUrl;
        this.mAvatarMediumUrl = avatarMediumUrl;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public String getAvatarSmallUrl() {
        return mAvatarSmallUrl;
    }

    public String getAvatarXSmallUrl() {
        return mAvatarXSmallUrl;
    }

    public String getAvatarMediumUrl() {
        return mAvatarMediumUrl;
    }
}
