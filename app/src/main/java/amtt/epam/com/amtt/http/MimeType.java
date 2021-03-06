package amtt.epam.com.amtt.http;

/**
 * Created by Artsiom_Kaliaha on 15.06.2015.
 * Holds mime types and compound file extensions
 */
public enum MimeType {

    TEXT_PLAIN(MediaType.TEXT_TYPE, "plain", "txt"),
    TEXT_HTML(MediaType.TEXT_TYPE, "html"),
    IMAGE_JPEG(MediaType.IMAGE_TYPE, "jpeg"),
    IMAGE_JPG(MediaType.IMAGE_TYPE, "jpg"),
    IMAGE_PNG(MediaType.IMAGE_TYPE, "png"),
    IMAGE_GIF(MediaType.IMAGE_TYPE, "gif");

    final String mCompoundType;
    final String mFileExtension;

    MimeType(String type, String subType, String fileExtension) {
        mCompoundType = type + "/" + subType;
        mFileExtension = "." + fileExtension;
    }

    MimeType(String type, String subType) {
        this(type, subType, subType);
    }

    public String getType() {
        return mCompoundType;
    }

    public String getFileExtension() {
        return mFileExtension;
    }

}
