package amtt.epam.com.amtt.ticket;

import amtt.epam.com.amtt.observer.AmttFileObserver;

import java.util.ArrayList;
import java.util.List;

public class ScreenshotManager {

    private static ArrayList<String> countryArray = AmttFileObserver.getImageArray();

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
            countries = new ArrayList<>();}
            if (countryArray != null) {
                for (int i = 0; i < countryArray.size(); i++) {
                    Screenshot screenshot = new Screenshot();
                    screenshot.name = countryArray.get(i);
                    screenshot.imageName = countryArray.get(i);
                    countries.add(screenshot);
                }
            }
        return countries;
    }

}
