package amtt.epam.com.amtt.processing;

import com.google.gson.Gson;

/**
 @author Iryna Monchanka
 @version on 26.03.2015
 */

public class GsonSerializeProcessor<T> implements Processor<T, String> {

    public static final String NAME = GsonSerializeProcessor.class.getName();

    @Override
    public String process(T inputObject){
        Gson gson = amtt.epam.com.amtt.processing.Gson.getInstance();
        String issue = null;
        try {
            issue = gson.toJson(inputObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return issue;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

}
