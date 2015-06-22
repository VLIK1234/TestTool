package amtt.epam.com.amtt.api;

import amtt.epam.com.amtt.api.JiraContentConst;
import amtt.epam.com.amtt.api.JiraGetContentCallback;

/**
 @author Iryna Monchanka
 @version on 15.05.2015
 */

public interface ContentLoadingCallback<Content> {
    void resultFromBackend(Content result, JiraContentConst tag, JiraGetContentCallback jiraGetContentCallback);
}
