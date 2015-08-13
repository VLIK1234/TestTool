package amtt.epam.com.amtt.api;

/**
 * @author Iryna Monchanka
 * @version on 15.05.2015
 */

public interface ContentLoadingCallback<Content, ContentCallback> {

    void resultFromBackend(Content content, ContentConst tag, GetContentCallback<ContentCallback> getContentCallback);

}
