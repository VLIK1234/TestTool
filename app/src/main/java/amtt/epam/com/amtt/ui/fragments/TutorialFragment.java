package amtt.epam.com.amtt.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;

/**
 * @author IvanBakach
 * @version on 14.10.2015
 */
public class TutorialFragment extends Fragment {

    public static final String KEY_DRAWABLE_ID = "drawableId";
    private ImageView mTutorialScreen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        mTutorialScreen = (ImageView) view.findViewById(R.id.iv_tutorial_screen);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTutorialScreen.setImageResource(getDrawableId());
    }

    public static TutorialFragment newInstance(int drawableId){
        TutorialFragment tutorialFragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_DRAWABLE_ID, drawableId);
        tutorialFragment.setArguments(args);
        return tutorialFragment;
    }

    private int getDrawableId() {
        return getArguments().getInt(KEY_DRAWABLE_ID);
    }
}
