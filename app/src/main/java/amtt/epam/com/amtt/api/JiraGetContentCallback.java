package amtt.epam.com.amtt.api;

/**
 * Created by Iryna_Monchanka on 5/13/2015.
 */
public interface JiraGetContentCallback<Content> {
    void resultOfDataLoading(Content result, JiraContentConst tagResult);
}
