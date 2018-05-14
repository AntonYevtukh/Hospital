package resource_managers;

import java.util.ResourceBundle;

public class UserDefaultsManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("user");
    private UserDefaultsManager() { }
    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
