package resource_managers;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MessageManager {
    private final static Map<String, ResourceBundle> resourceBundleMap = Map.of(
            "ru", ResourceBundle.getBundle("messages", new Locale("ru")),
            "en", ResourceBundle.getBundle("messages", new Locale("en")));
    private MessageManager() { }
    public static String getProperty(String key, String language) {
        return resourceBundleMap.get(language).getString(key);
    }
}
