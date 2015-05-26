package amtt.epam.com.amtt.ticket;

import java.util.ArrayList;
import java.util.List;

public class ScreenshotManager {

    private static String[] countryArray = {"Australia", "China", "Italy", "Japan"};

    private static ScreenshotManager mInstance;
    private List<Screenshot> countries;

    public static ScreenshotManager getInstance() {
        if (mInstance == null) {
            mInstance = new ScreenshotManager();
        }

        return mInstance;
    }

    public List<Screenshot> getCountries() {
        if (countries == null) {
            countries = new ArrayList<>();

            for (String countryName : countryArray) {
                Screenshot screenshot = new Screenshot();
                screenshot.name = countryName;
                screenshot.imageName = countryName.replaceAll("\\s+","").toLowerCase();
                countries.add(screenshot);
            }
        }

        return countries;
    }

}
