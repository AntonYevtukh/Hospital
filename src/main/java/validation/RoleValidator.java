package validation;

import model.entities.Role;
import resource_managers.RegexManager;

import java.util.ArrayList;
import java.util.List;

public class RoleValidator implements EntityValidator<Role> {

    private static RoleValidator instance;

    public static RoleValidator getInstance() {
        if (instance == null) {
            synchronized (RoleValidator.class) {
                if (instance == null)
                    instance = new RoleValidator();
            }
        }
        return instance;
    }

    private RoleValidator() {

    }

    @Override
    public List<String> validate(Role role) {
        List<String> errorMessageKeys = new ArrayList<>(1);
        if (role.getName() == null || !role.getName().matches(RegexManager.getProperty("regex.role.name"))) {
            errorMessageKeys.add("validation.role.name");
        }

        return errorMessageKeys;
    }
}
