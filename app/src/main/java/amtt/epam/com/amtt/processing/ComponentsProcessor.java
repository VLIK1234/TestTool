package amtt.epam.com.amtt.processing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.bo.JComponentsResponse;
import amtt.epam.com.amtt.bo.project.JComponent;

/**
 * @author Iryna Monchanka
 * @version on 23.06.2015
 */
public class ComponentsProcessor implements Processor<JComponentsResponse, HttpEntity> {

    public static final String NAME = ComponentsProcessor.class.getName();
    @Override
    public JComponentsResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(_response).getAsJsonArray();
        JComponentsResponse componentsResponse = new JComponentsResponse();
        ArrayList<JComponent> jComponents = new ArrayList<>();
        for (JsonElement obj : jsonArray) {
            JComponent jComponent = Gson.getInstance().fromJson(obj, JComponent.class);
            jComponents.add(jComponent);
        }
        componentsResponse.setComponents(jComponents);
        JiraContent.getInstance().setComponentsNames(componentsResponse.getComponentsNames());
        return componentsResponse;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
