package amtt.epam.com.amtt.fragment;

import android.app.Fragment;
import android.widget.ProgressBar;

/**
 * Created by Artsiom_Kaliaha on 05.05.2015.
 */
public class BaseFragment extends Fragment {

    ProgressBar mProgressBar;

    void setProgressVisibility(int visibilityState) {
        mProgressBar.setVisibility(visibilityState);
    }

}
