package amtt.epam.com.amtt.bo.issue.user;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JiraUserGroup {

    @SerializedName("user_group_size")
    private int mSize;
    @SerializedName("items")
    private ArrayList<JiraUserGroupItem> mItems;

    public JiraUserGroup() {
    }

    public JiraUserGroup(int size, ArrayList<JiraUserGroupItem> items) {
        this.mSize = size;
        this.mItems = items;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public ArrayList<JiraUserGroupItem> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<JiraUserGroupItem> items) {
        this.mItems = items;
    }
}