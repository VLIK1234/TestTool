package amtt.epam.com.amtt.processing;

import com.google.gson.Gson;


/**
 * Created by Irina Monchenko on 26.03.2015.
 */
public class IssueGsonProcessor<T> implements Processor<String, T> {

    @Override
    public String process(T inputObject) throws Exception {
        Gson gson = amtt.epam.com.amtt.processing.Gson.getInstance();
        String issue = null;
        try {
            issue = gson.toJson(inputObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return issue;
    }


}
