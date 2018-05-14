package resource_managers;

import java.util.ResourceBundle;

public class RegexManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("validation.regex");
    private RegexManager() { }
    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
