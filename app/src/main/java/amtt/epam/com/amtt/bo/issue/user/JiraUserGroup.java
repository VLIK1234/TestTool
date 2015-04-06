package amtt.epam.com.amtt.bo.issue.user;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JiraUserGroup {

    @SerializedName("size")
    private int size;
    @SerializedName("items")
    private ArrayList<JiraUserGroupItem> items;

    public JiraUserGroup() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<JiraUserGroupItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<JiraUserGroupItem> items) {
        this.items = items;
    }
}
