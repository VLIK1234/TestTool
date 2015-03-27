package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.bo.Entity;
import com.google.gson.Gson;

/**
 * Created by shiza on 26.03.2015.
 */
public class IssueGsonProcessor <T extends Entity> implements Processor<String, T> {

    @Override
    public String process(T inputObject) throws Exception {
        Gson gson = buildGson();
        String issue = null;
        try{
        issue = gson.toJson(inputObject);

    } catch (Exception e) {
        e.printStackTrace();
    }
        return issue;
    }

    protected Gson buildGson() {
        return new Gson();
    }


}
