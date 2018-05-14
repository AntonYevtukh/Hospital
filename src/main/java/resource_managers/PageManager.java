package resource_managers;

import java.util.ResourceBundle;
public class PageManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("pages");
    private PageManager() { }
    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
