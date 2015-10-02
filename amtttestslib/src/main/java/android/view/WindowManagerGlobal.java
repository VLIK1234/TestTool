package android.view;

/**
 * @author IvanBakach
 * @version on 02.10.2015
 */
public class WindowManagerGlobal {
    private static WindowManagerGlobal sDefaultWindowManager;

    public static WindowManagerGlobal getInstance() {
        synchronized (WindowManagerGlobal.class) {
            if (sDefaultWindowManager == null) {
                sDefaultWindowManager = new WindowManagerGlobal();
            }
            return sDefaultWindowManager;
        }
    }
}
