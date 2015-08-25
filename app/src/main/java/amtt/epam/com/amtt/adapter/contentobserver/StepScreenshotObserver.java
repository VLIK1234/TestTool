package amtt.epam.com.amtt.adapter.contentobserver;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import amtt.epam.com.amtt.adapter.AttachmentAdapter;

/**
 * @author Iryna Monchanka
 * @version on 8/19/2015
 */
public class StepScreenshotObserver extends ContentObserver {

    private final AttachmentAdapter mAdapter;

    public StepScreenshotObserver(Handler handler, AttachmentAdapter attachmentAdapter) {
        super(handler);
        mAdapter = attachmentAdapter;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        mAdapter.mDataChangedListener.onReloadData();
    }

}
