package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.bo.project.JPriority;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 04.05.2015.
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
}
