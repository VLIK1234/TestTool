package amtt.epam.com.amtt.bo;

import java.util.ArrayList;
import java.util.HashMap;

import amtt.epam.com.amtt.bo.project.JPriority;

/**
 @author Iryna Monchanka
 @version on 04.05.2015
 */

public class JPriorityResponse {

    private ArrayList<JPriority> mPriorities;

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

    public HashMap<String, String> getPriorityNames() {
        HashMap<String, String> mPriorityNames;
        if (mPriorities != null) {
            mPriorityNames = new HashMap<>();
            for (int i = 0; i < mPriorities.size(); i++) {
                mPriorityNames.put(mPriorities.get(i).getJiraId(), mPriorities.get(i).getName());
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
