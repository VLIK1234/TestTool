package amtt.epam.com.amtt.api;

/**
 * Created by Iryna_Monchanka on 5/13/2015.
 */
public interface JiraContentCallback<Content> {
    void success(Content result, JiraContentConst tagResult);
}
