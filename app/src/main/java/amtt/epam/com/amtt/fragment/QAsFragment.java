package amtt.epam.com.amtt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import amtt.epam.com.amtt.R;

/**
 * Created by Artsiom_Kaliaha on 30.04.2015.
 */
public class QAsFragment extends Fragment {

    private ListView mListView;
    private ProgressBar mProgressBar;

    public QAsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_qas, container, false);

        mListView = (ListView)layout.findViewById(android.R.id.list);
        mProgressBar = (ProgressBar)layout.findViewById(R.id.progress);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
