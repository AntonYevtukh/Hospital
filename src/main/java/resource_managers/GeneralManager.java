package resource_managers;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class GeneralManager {
    private final static Map<String, ResourceBundle> resourceBundleMap = Map.of(
            "ru", ResourceBundle.getBundle("general", new Locale("ru")),
            "en", ResourceBundle.getBundle("general", new Locale("en")));
    private GeneralManager() { }
    public static String getProperty(String key, String language) {
        return resourceBundleMap.get(language).getString(key);
    }
}
