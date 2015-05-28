package amtt.epam.com.amtt.helper;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Ivan_Bakach on 28.05.2015.
 */
public enum HelpTakeScreen {
    NEXUS(Constants.NEXUS, ContextHolder.getContext().getString(R.string.nexus_help_message)),
    SAMSUNG(Constants.SAMSUNG, ContextHolder.getContext().getString(R.string.samsung_help_message)),
    HTC(Constants.HTC, ContextHolder.getContext().getString(R.string.htc_help_message)),
    XIAOMI(Constants.XIAOMI, ContextHolder.getContext().getString(R.string.xiaomi_help_message)),
    ALL(Constants.ALL, ContextHolder.getContext().getString(R.string.all_help_message));

    private String key;
    private String value;

    HelpTakeScreen(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getKey().toUpperCase();
    }

    public static class Constants {
        public static final String NEXUS = "NEXUS";
        public static final String SAMSUNG = "SAMSUNG";
        public static final String HTC = "HTC";
        public static final String XIAOMI = "XIAOMI";
        public static final String ALL = "ALL";
    }
}