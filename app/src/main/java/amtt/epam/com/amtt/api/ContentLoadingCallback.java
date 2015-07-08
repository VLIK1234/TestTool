package amtt.epam.com.amtt.api;

/**
 * @author Iryna Monchanka
 * @version on 15.05.2015
 */

public interface ContentLoadingCallback<Content> {

    void resultFromBackend(Content result, ContentConst tag, GetContentCallback getContentCallback);

}
