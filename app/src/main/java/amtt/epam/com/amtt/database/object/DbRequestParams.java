package amtt.epam.com.amtt.database.object;

import java.util.List;

import amtt.epam.com.amtt.database.DbRequestType;

/**
 * @author Iryna Monchenko
 * @version on 21.08.2015
 */

public class DbRequestParams<Entity extends DatabaseEntity> {

    private DbRequestType mRequestType;
    private Entity mObject;
    private List<Entity> mObjectList;
    private String[] mProjection;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mSortOrder;

    public DbRequestParams(Entity object, DbRequestType requestType) {
        mObject = object;
        mRequestType = requestType;
    }

    public DbRequestParams(List<Entity> objectList, DbRequestType requestType) {
        mObjectList = objectList;
        mRequestType = requestType;
    }

    public DbRequestParams(Entity object, String selection, String[] selectionArgs, DbRequestType requestType) {
        mObject = object;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mRequestType = requestType;
    }

    public DbRequestParams(Entity object, String[] projection, String selection, String[] selectionArgs,
                           String sortOrder, DbRequestType requestType) {
        mObject = object;
        mProjection = projection;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mSortOrder = sortOrder;
        mRequestType = requestType;
    }

    public DbRequestType getRequestType() {
        return mRequestType;
    }

    public void setRequestType(DbRequestType requestType) {
        mRequestType = requestType;
    }

    public Entity getObject() {
        return mObject;
    }

    public void setObject(Entity object) {
        this.mObject = object;
    }

    public List<Entity> getObjectList() {
        return mObjectList;
    }

    public void setObjectList(List<Entity> objectList) {
        mObjectList = objectList;
    }

    public String[] getProjection() {
        return mProjection;
    }

    public void setProjection(String[] projection) {
        this.mProjection = projection;
    }

    public String getSelection() {
        return mSelection;
    }

    public void setSelection(String selection) {
        this.mSelection = selection;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        this.mSelectionArgs = selectionArgs;
    }

    public String getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.mSortOrder = sortOrder;
    }
}
