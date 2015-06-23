package amtt.epam.com.amtt.bo;

import java.util.ArrayList;
import java.util.HashMap;

import amtt.epam.com.amtt.bo.project.JComponent;

/**
 * @author Iryna Monchanka
 * @version on 23.06.2015
 */

public class JComponentsResponse {

    private ArrayList<JComponent> mComponents;

    public JComponentsResponse() {
    }

    public JComponentsResponse(ArrayList<JComponent> components) {
        this.mComponents = components;
    }

    public HashMap<String, String> getComponentsNames() {
        HashMap<String, String> mComponentsNames;
        if (mComponents != null) {
            mComponentsNames = new HashMap<>();
            for (int i = 0; i < mComponents.size(); i++) {
                mComponentsNames.put(mComponents.get(i).getId(), mComponents.get(i).getName());
            }
            return mComponentsNames;
        } else {
            return null;
        }
    }

    public ArrayList<JComponent> getComponents() {
        return mComponents;
    }

    public void setComponents(ArrayList<JComponent> components) {
        this.mComponents = components;
    }

    public JComponent getComponentByName(String componentName) {
        JComponent issueComponent = null;
        for (JComponent component : mComponents) {
            if (component.getName().equals(componentName)) {
                issueComponent = component;
            }
        }
        return issueComponent;
    }
}
