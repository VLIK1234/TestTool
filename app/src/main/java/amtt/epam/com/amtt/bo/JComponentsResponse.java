package amtt.epam.com.amtt.bo;

import java.util.HashMap;
import java.util.List;

import amtt.epam.com.amtt.bo.project.JComponent;

/**
 * @author Iryna Monchanka
 * @version on 23.06.2015
 */

public class JComponentsResponse {

    private List<JComponent> mComponents;

    public JComponentsResponse() {
    }

    public HashMap<String, String> getComponentsNames() {
        HashMap<String, String> mComponentsNames;
        if (mComponents != null) {
            mComponentsNames = new HashMap<>();
            for (int i = 0; i < mComponents.size(); i++) {
                mComponentsNames.put(mComponents.get(i).getJiraId(), mComponents.get(i).getName());
            }
            return mComponentsNames;
        } else {
            return null;
        }
    }

    public void setComponents(List<JComponent> components) {
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
