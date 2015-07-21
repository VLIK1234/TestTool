package amtt.epam.com.amtt.googleapi.bo;

import android.database.Cursor;

import amtt.epam.com.amtt.database.object.DatabaseEntity;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */
public class GBase<T> extends DatabaseEntity<T> {

    protected String mIdLink;
    protected String mUpdated;
    protected String mTitle;
    protected GLink mSelfLink;

    public GBase() {
    }

    public GBase(String idLink, String updated, String title, GLink selfLink) {
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

    public GLink getSelfLink() {
        return mSelfLink;
    }

    public void setSelfLink(GLink selfLink) {
        this.mSelfLink = selfLink;
    }

    @Override
    public T parse(Cursor cursor) {
        return null;
    }
}
