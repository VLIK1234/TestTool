package amtt.epam.com.amtt.bo;

import java.util.ArrayList;

import amtt.epam.com.amtt.bo.project.JPriority;

/**
 * Created by Iryna_Monchanka on 04.05.2015.
 */
public class JPriorityResponse {

    private ArrayList<JPriority> mPriorities;
    private ArrayList<String> mPriorityNames;

    public JPriorityResponse(){}

    public JPriorityResponse(ArrayList<JPriority> priorities) {
        this.mPriorities = priorities;
    }

    public ArrayList<JPriority> getPriorities() {
        return mPriorities;
    }

    public void setPriorities(ArrayList<JPriority> priorities) {
        this.mPriorities = priorities;
    }

    public ArrayList<String> getPriorityNames() {
        if (mPriorities != null) {
            mPriorityNames = new ArrayList<>();
            for (int i = 0; i < mPriorities.size(); i++) {
                mPriorityNames.add(mPriorities.get(i).getName());
            }
            return mPriorityNames;
        } else {
            return null;
        }
    }

    public JPriority getPriorityByName(String priorityName) {
        JPriority priority = null;
        for (JPriority jPriority : mPriorities) {
            if (jPriority.getName().equals(priorityName)) {
                priority = jPriority;
            }
        }
        return priority;
    }
}
