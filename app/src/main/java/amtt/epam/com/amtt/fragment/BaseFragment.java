package amtt.epam.com.amtt.fragment;

import android.app.Fragment;
import android.widget.ProgressBar;

/**
 * Created by Artsiom_Kaliaha on 05.05.2015.
 */
public class BaseFragment extends Fragment {

    static final int CURSOR_LOADER_ID = 0;
    static final int NO_FLAGS = 0;

    ProgressBar mProgressBar;

    void setProgressVisibility(int visibilityState) {
        mProgressBar.setVisibility(visibilityState);
    }

}
