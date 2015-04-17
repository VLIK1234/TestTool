package amtt.epam.com.amtt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 17.04.2015.
 */
public class AuthButton extends Button {
    private static final int[] STATE_AUTH = { R.attr.state_auth };

    private boolean isAuth = false;

    public AuthButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Android calls this method to know the current drawable state of the view.
    // It starts with an "extraSpace" of 0 in View.java, and each inherited view adds its new state.
    // We add just one more state, hence, we create a new array of size "extraSpace + 1".
    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        // Ask the parent to add its default states.
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

        // If we are private, add the state to array of states.
        // If not added, the value will be treated as false.
        // mergeDrawableStates() takes care of resolving the duplicates.
        if (this.isAuth)
            mergeDrawableStates(drawableState, STATE_AUTH);

        // Return the new drawable state.
        return drawableState;
    }

    public void setAuthStatee(boolean isAuth) {
        // If we flip the current state of private mode, record the value
        // and inform Android to refresh the drawable state.
        // This will in turn invalidate() the view.
        if (this.isAuth != isAuth) {
            this.isAuth = isAuth;
            refreshDrawableState();
        }
    }
}
