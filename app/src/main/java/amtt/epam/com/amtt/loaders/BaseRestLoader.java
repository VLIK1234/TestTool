package amtt.epam.com.amtt.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;

/**
 * Created by shiza on 02.04.2015.
 */
public abstract class BaseRestLoader<T> extends AsyncTaskLoader<T> {
    private static final String TAG = BaseRestLoader.class.getSimpleName();
    private T mData;

    public BaseRestLoader(Context ctx) {
        super(ctx);
    }

    @Override
    public T loadInBackground() {
        T data = null;

        try {
            data = retrieveResponse();
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }

        return data;
    }

    protected abstract T retrieveResponse() throws IOException;

    @Override
    public void deliverResult(T data) {
        if (isReset()) {

            releaseResources(data);
            return;
        }

        T oldData = mData;
        mData = data;

        if (isStarted()) {

            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {

        cancelLoad();

    }

    @Override
    protected void onReset() {

        onStopLoading();

        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }

    }

    @Override
    public void onCanceled(T data) {

        super.onCanceled(data);

        releaseResources(data);
    }

    private void releaseResources(T data) {

    }

}
