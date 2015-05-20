package amtt.epam.com.amtt.bo.user;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JUserGroup {

    @SerializedName("size")
    private int mSize;
    @SerializedName("items")
    private ArrayList<JUserGroupItem> mItems;

    public JUserGroup() {
    }

    public JUserGroup(int size, ArrayList<JUserGroupItem> items) {
        this.mSize = size;
        this.mItems = items;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public ArrayList<JUserGroupItem> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<JUserGroupItem> items) {
        this.mItems = items;
    }
}