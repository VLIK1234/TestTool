package amtt.epam.com.amtt.ui.views.paintview;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * @author IvanBakach
 * @version on 08.09.2015
 */
public final class DrawnPath extends DrawObject {

    private final Path mPath;
    private final PaintMode mPaintMode;

    public DrawnPath(Path path, Paint paint, PaintMode paintMode) {
        super(paint);
        mPath = path;
        mPaintMode = paintMode;
    }

    public Path getPath() {
        return mPath;
    }

    public PaintMode getPaintMode() {
        return mPaintMode;
    }

    public void addPath(Path path) {
        mPath.addPath(path);
    }
}
