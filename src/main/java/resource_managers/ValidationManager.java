package resource_managers;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ValidationManager {
    private final static Map<String, ResourceBundle> resourceBundleMap = Map.of(
            "ru", ResourceBundle.getBundle("validation.validation", new Locale("ru")),
            "en", ResourceBundle.getBundle("validation.validation", new Locale("en")));
    private ValidationManager() { }
    public static String getProperty(String key, String language) {
        return resourceBundleMap.get(language).getString(key);
    }
}
