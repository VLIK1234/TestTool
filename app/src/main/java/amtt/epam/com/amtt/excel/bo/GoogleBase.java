package amtt.epam.com.amtt.excel.bo;

import android.database.Cursor;

import amtt.epam.com.amtt.database.object.DatabaseEntity;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GoogleBase<T> extends DatabaseEntity<T> {

    protected String mIdLink;
    protected String mUpdated;
    protected String mTitle;
    protected GoogleLink mSelfLink;

    public GoogleBase() {
    }

    public GoogleBase(String idLink, String updated, String title, GoogleLink selfLink) {
        this.mIdLink = idLink;
        this.mUpdated = updated;
        this.mTitle = title;
        this.mSelfLink = selfLink;
    }

    public String getIdLink() {
        return mIdLink;
    }

    public void setIdLink(String idLink) {
        this.mIdLink = idLink;
    }

    public String getUpdated() {
        return mUpdated;
    }

    public void setUpdated(String updated) {
        this.mUpdated = updated;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public GoogleLink getSelfLink() {
        return mSelfLink;
    }

    public void setSelfLink(GoogleLink selfLink) {
        this.mSelfLink = selfLink;
    }

    @Override
    public T parse(Cursor cursor) {
        return null;
    }
}
