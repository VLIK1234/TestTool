package amtt.epam.com.amtt.bo.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 4/3/2015.
 */
public class JUserGroupItem {

    @SerializedName("name")
    private String mName;
    @SerializedName("self")
    private String mSelf;

    public JUserGroupItem() {
    }

    public JUserGroupItem(String name, String self) {
        this.mName = name;
        this.mSelf = self;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
    }
}