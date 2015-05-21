package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Irina Monchenko on 30.03.2015.
 */
public class JAvatarUrls {

    @SerializedName("48x48")
    private String mAvatarUrl;
    @SerializedName("24x24")
    private String mAvatarSmallUrl;
    @SerializedName("16x16")
    private String mAvatarXSmallUrl;
    @SerializedName("32x32")
    private String mAvatarMediumUrl;

    public JAvatarUrls() {
    }

    public JAvatarUrls(String avatarUrl, String avatarSmallUrl, String avatarXSmallUrl, String avatarMediumUrl) {
        this.mAvatarUrl = avatarUrl;
        this.mAvatarSmallUrl = avatarSmallUrl;
        this.mAvatarXSmallUrl = avatarXSmallUrl;
        this.mAvatarMediumUrl = avatarMediumUrl;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public String getAvatarSmallUrl() {
        return mAvatarSmallUrl;
    }

    public void setAvatarSmallUrl(String avatarSmallUrl) {
        this.mAvatarSmallUrl = avatarSmallUrl;
    }

    public String getAvatarXSmallUrl() {
        return mAvatarXSmallUrl;
    }

    public void setAvatarXSmallUrl(String avatarXSmallUrl) {
        this.mAvatarXSmallUrl = avatarXSmallUrl;
    }

    public String getAvatarMediumUrl() {
        return mAvatarMediumUrl;
    }

    public void setAvatarMediumUrl(String avatarMediumUrl) {
        this.mAvatarMediumUrl = avatarMediumUrl;
    }
}
